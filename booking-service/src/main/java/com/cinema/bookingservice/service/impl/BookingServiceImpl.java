package com.cinema.bookingservice.service.impl;

import com.cinema.bookingservice.dto.request.BookingSeatItem;
import com.cinema.bookingservice.dto.request.CreateBookingRequest;
import com.cinema.bookingservice.entity.Booking;
import com.cinema.bookingservice.entity.BookingSeat;
import com.cinema.bookingservice.repository.BookingRepository;
import com.cinema.bookingservice.repository.BookingSeatRepository;
import com.cinema.bookingservice.service.BookingService;
import com.cinema.common.base.ServiceResult;
import com.cinema.common.constants.CommonConstants;
import com.cinema.common.enums.BookingStatus;
import com.cinema.common.exception.BadRequestException;
import com.cinema.common.exception.ErrorCode;
import com.cinema.common.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BookingServiceImpl implements BookingService {
    private final RedisService redisService;
    private final BookingRepository bookingRepository;
    private final BookingSeatRepository bookingSeatRepository;

    @Override
    public ServiceResult book(CreateBookingRequest request) {
        log.debug("BookingServiceImpl book");
        List<String> keys = new ArrayList<>();
        // check duplicate in redis
        for (BookingSeatItem item : request.getSeats()) {
            String seatKey = generateSeatKey(request.getShowtimeId(), item.getSeatId());
            Object data = redisService.getValue(seatKey);
            if (data != null
//                    && !Objects.equals(Long.parseLong(data.toString()), request.getUserId())
            ) {
                log.warn("Seat {} for showtime {} is already booked", item.getSeatId(), request.getShowtimeId());
                rollbackRedisBookings(keys);
                throw new BadRequestException(ErrorCode.SEAT_ALREADY_BOOKED);
            }

            redisService.setValueWithExpireTime(seatKey, request.getUserId(), 15, TimeUnit.MINUTES);
            keys.add(seatKey);
        }

        String bookingKey = String.format(CommonConstants.RedisKey.BOOKING_KEY, request.getUserId(), request.getShowtimeId());
        redisService.setValueWithExpireTime(bookingKey, request.getUserId(), 15, TimeUnit.MINUTES);

        // check duplicate in DB
        Optional<Integer> checkDuplicated = bookingRepository.checkSeatAlreadyBooked(
                request.getShowtimeId(), request.getUserId(),
                request.getSeats().stream().map(BookingSeatItem::getSeatId).collect(Collectors.toList())
        );
        if (checkDuplicated.isPresent()) {
            throw new BadRequestException(ErrorCode.SEAT_ALREADY_BOOKED);
        }

        Optional<Booking> bookingOpt = bookingRepository.findByUserIdAndShowtimeIdAndStatus(
                request.getUserId(),
                request.getShowtimeId(),
                BookingStatus.HOLDING.name()
        );
        bookingOpt.ifPresent(bookingRepository::delete);

        Booking booking = Booking.builder()
                .userId(request.getUserId())
                .showtimeId(request.getShowtimeId())
                .status(BookingStatus.HOLDING.name())
                .totalPrice(request.getTotalPrice())
                .expiredAt(LocalDateTime.now().plusMinutes(15))
                .build();
        bookingRepository.save(booking);

        List<BookingSeat> bookingSeats = new ArrayList<>();
        for (BookingSeatItem item : request.getSeats()) {
            BookingSeat bookingSeat = BookingSeat.builder()
                    .seatId(item.getSeatId())
                    .price(item.getPrice())
                    .bookingId(booking.getId())
                    .build();
            bookingSeats.add(bookingSeat);
        }
        bookingSeatRepository.saveAll(bookingSeats);

        log.info("Booking has been saved successfully");
        return ServiceResult.ok();
    }

    @Override
    public ServiceResult handleBookingExpired(String redisKey) {
        // BOOKING_USER_ID_1_SHOWTIME_ID_2
        if (redisKey.startsWith("BOOKING_USER_ID_")) {
            String[] parts = redisKey.split("_");
            Long userId = Long.parseLong(parts[3]);
            Long showTimeId = Long.parseLong(parts[6]);

            Optional<Booking> bookingOpt = bookingRepository.findByUserIdAndShowtimeIdAndStatus(
                    userId,
                    showTimeId,
                    BookingStatus.HOLDING.name()
            );
            if (bookingOpt.isPresent()) {
                bookingSeatRepository.deleteByBookingId(bookingOpt.get().getId());
                bookingRepository.delete(bookingOpt.get());
            }
        }
        return null;
    }

    private String generateSeatKey(Long showtimeId, Long seatId) {
        return String.format(CommonConstants.RedisKey.SEAT_KEY, showtimeId, seatId);
    }

    private void rollbackRedisBookings(List<String> keys) {
        for (String key : keys) {
            redisService.removeValue(key);
        }
    }
}
