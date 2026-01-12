package com.cinema.showtimeservice.service.impl;

import com.cinema.common.base.ServiceResult;
import com.cinema.common.constants.CommonConstants;
import com.cinema.common.dto.RefreshTokenRequest;
import com.cinema.common.enums.BookingStatus;
import com.cinema.common.enums.ShowtimeStatus;
import com.cinema.common.exception.BadRequestException;
import com.cinema.common.exception.ErrorCode;
import com.cinema.common.service.RedisService;
import com.cinema.security.service.CurrentUserService;
import com.cinema.showtimeservice.dto.request.SearchShowtimeRequest;
import com.cinema.showtimeservice.dto.request.UpdateShowtimeRequest;
import com.cinema.showtimeservice.dto.response.RoomSeatMapItem;
import com.cinema.showtimeservice.dto.response.RoomSeatMapResponse;
import com.cinema.showtimeservice.dto.response.ShowtimeDetailResponse;
import com.cinema.showtimeservice.dto.response.ShowtimeItemResponse;
import com.cinema.showtimeservice.entity.*;
import com.cinema.showtimeservice.dto.request.CreateShowtimeRequest;
import com.cinema.showtimeservice.mapper.ShowtimeMapper;
import com.cinema.showtimeservice.mapper.ShowtimePriceMapper;
import com.cinema.showtimeservice.repository.*;
import com.cinema.showtimeservice.service.ShowtimeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ShowtimeServiceImpl implements ShowtimeService {
    private final ShowtimeRepository showtimeRepository;
    private final MovieRepository movieRepository;
    private final ShowtimePriceRepository showtimePriceRepository;
    private final ShowtimeMapper showtimeMapper;
    private final ShowtimePriceMapper showtimePriceMapper;

    private final RoomRepository roomRepository;
    private final SeatRepository seatRepository;
    private final RedisService redisService;

    private final RestTemplate restTemplate;

    @Override
    public ServiceResult create(CreateShowtimeRequest request) {
        log.debug("create showtime: {}", request);
        Optional<Movie> movieOpt = movieRepository.findById(request.getMovieId());
        if (movieOpt.isEmpty()) {
            log.warn("Movie with id {} not found", request.getMovieId());
            return ServiceResult.fail(ErrorCode.MOVIE_NOT_FOUND);
        }

        Integer movieDuration = movieOpt.get().getDuration();
        LocalDateTime endTime = request.getStartTime().plusMinutes(request.getBufferMinutes()).plusMinutes(movieDuration);
        List<Long> freeRoomIds = showtimeRepository.getFreeRoom(
                request.getStartTime(),
                endTime
        );

        if (freeRoomIds.isEmpty()) {
            log.warn("No available rooms for the requested showtime");
            return ServiceResult.fail(ErrorCode.SHOWTIME_NOT_SUITABLE_ROOM);
        }

        Optional<Long> roomIdOpt = showtimeRepository.getSuitableRoom(freeRoomIds, request.getStartTime().toLocalDate());
        if (roomIdOpt.isEmpty()) {
            log.warn("No suitable room found for the requested showtime on date {}", request.getStartTime().toLocalDate());
            return ServiceResult.fail(ErrorCode.SHOWTIME_NOT_SUITABLE_ROOM);
        }

        Showtime showtime = Showtime.builder()
                .bufferTime(request.getBufferMinutes())
                .startTime(request.getStartTime())
                .movieId(request.getMovieId())
                .roomId(roomIdOpt.get())
                .endTime(endTime)
                .status(ShowtimeStatus.ACTIVE.name())
                .build();
        showtimeRepository.save(showtime);

        // Save showtime prices if provided
        if (request.getPrices() != null && !request.getPrices().isEmpty()) {
            List<ShowtimePrice> prices = new ArrayList<>();
            request.getPrices().forEach(priceReq -> {
                 ShowtimePrice price = ShowtimePrice.builder()
                         .showtimeId(showtime.getId())
                         .seatType(priceReq.getSeatType())
                         .price(priceReq.getPrice())
                         .build();
                 prices.add(price);
            });
            showtimePriceRepository.saveAll(prices);
        }

        log.debug("Created showtime: {}", showtime);
        return ServiceResult.ok();
    }

    @Override
    public ServiceResult getDetails(Long showtimeId) {
        log.debug("Get showtime details for id: {}", showtimeId);
        Optional<Showtime> showtimeOpt = showtimeRepository.findById(showtimeId);
        if (showtimeOpt.isEmpty()) {
            log.warn("Showtime with id {} not found", showtimeId);
            return ServiceResult.fail(ErrorCode.SHOWTIME_NOT_FOUND);
        }

        Showtime showtime = showtimeOpt.get();
        ShowtimeDetailResponse response = showtimeMapper.toShowtimeDetailResponse(showtime);
        List<ShowtimePrice> prices = showtimePriceRepository.findByShowtimeId(showtimeId);
        response.setPrices(showtimePriceMapper.toShowtimePriceItems(prices));

        return ServiceResult.ok(response);
    }

    @Override
    public ServiceResult update(UpdateShowtimeRequest request) {
        log.debug("Update showtime: {}", request);
        Optional<Showtime> showtimeOpt = showtimeRepository.findById(request.getId());
        if (showtimeOpt.isEmpty()) {
            log.warn("Showtime with id {} not found", request.getId());
            return ServiceResult.fail(ErrorCode.SHOWTIME_NOT_FOUND);
        }
        Optional<Movie> movieOpt = movieRepository.findById(request.getMovieId());
        if (movieOpt.isEmpty()) {
            log.warn("Movie with id {} not found", request.getMovieId());
            return ServiceResult.fail(ErrorCode.MOVIE_NOT_FOUND);
        }

        Showtime showtime = showtimeOpt.get();
        LocalDateTime endTime = request.getStartTime().plusMinutes(request.getBufferTime()).plusMinutes(movieOpt.get().getDuration());
        Optional<Integer> duplicateOpt = showtimeRepository.checkDuplicateTimeSlot(
                request.getId(),
                request.getRoomId(),
                request.getStartTime(),
                endTime
        );
        if (duplicateOpt.isPresent()) {
            log.warn("Duplicate time slot for showtime id {}", request.getId());
            return ServiceResult.fail(ErrorCode.SHOWTIME_DUPLICATE_TIME_SLOT);
        }

        showtime.setStartTime(request.getStartTime());
        showtime.setEndTime(endTime);
        showtime.setBufferTime(request.getBufferTime());
        showtime.setMovieId(request.getMovieId());

        showtimeRepository.save(showtime);

        // process showtime prices
        showtimePriceRepository.deleteByShowtimeId(request.getId());
        List<ShowtimePrice> newPrices = new ArrayList<>();
        request.getPrices().forEach(priceReq -> {
            ShowtimePrice price = ShowtimePrice.builder()
                    .showtimeId(showtime.getId())
                    .seatType(priceReq.getSeatType())
                    .price(priceReq.getPrice())
                    .build();
            newPrices.add(price);
        });
        showtimePriceRepository.saveAll(newPrices);

        log.debug("Updated showtime: {}", showtime);
        return ServiceResult.ok();
    }

    @Override
    public ServiceResult searchShowTime(SearchShowtimeRequest request) {
        List<ShowtimeItemResponse> responses = showtimeRepository.searchShowTime(request);
        return ServiceResult.ok(responses);
    }

    @Override
    public ServiceResult getShowtimeMap(Long showtimeId) {
        Optional<Showtime> showtimeOptional = showtimeRepository.findById(showtimeId);
        if (showtimeOptional.isEmpty()) {
            throw new BadRequestException(ErrorCode.SHOWTIME_NOT_FOUND);
        }

        Showtime showtime = showtimeOptional.get();
        Long roomId = showtime.getRoomId();
        Optional<Room> roomOptional = roomRepository.findById(roomId);
        if (roomOptional.isEmpty()) {
            throw new BadRequestException(ErrorCode.ROOM_NOT_FOUND);
        }

        RoomSeatMapResponse response = new RoomSeatMapResponse();
        BeanUtils.copyProperties(roomOptional.get(), response);

        List<Seat> seats = seatRepository.findByRoomId(roomId);
        List<RoomSeatMapItem> seatMapItems = new ArrayList<>();

        Set<String> redisSeatKey = redisService.findKeysWithPrefix(CommonConstants.RedisKey.SEAT_KEY_PREFIX + showtimeId);
        List<Long> seatPaidList = seatRepository.findBookingSeat(showtimeId);
        for (String item : redisSeatKey) {
            // SHOWTIME_1_SEAT_2
            String[] parts = item.split("_");
            seatPaidList.add(Long.valueOf(parts[3]));
        }

        for (Seat seat : seats) {
            RoomSeatMapItem item = new RoomSeatMapItem();
            BeanUtils.copyProperties(seat, item);
            if (seatPaidList.contains(seat.getId())) {
                item.setBookingStatus(BookingStatus.PAID);
            }
            seatMapItems.add(item);
        }
        response.setSeatMap(seatMapItems);

        return ServiceResult.ok(response);
    }
}
