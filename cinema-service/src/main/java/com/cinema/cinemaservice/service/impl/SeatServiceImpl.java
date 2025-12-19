package com.cinema.cinemaservice.service.impl;

import com.cinema.common.base.ServiceResult;
import com.cinema.common.exception.ErrorCode;
import com.cinema.cinemaservice.dto.request.seat.UpdateSeatTypeRequest;
import com.cinema.cinemaservice.dto.response.seat.RoomSeatMapResponse;
import com.cinema.cinemaservice.entity.Room;
import com.cinema.cinemaservice.entity.Seat;
import com.cinema.cinemaservice.mapper.RoomMapper;
import com.cinema.cinemaservice.mapper.SeatMapper;
import com.cinema.cinemaservice.repository.RoomRepository;
import com.cinema.cinemaservice.repository.SeatRepository;
import com.cinema.cinemaservice.service.SeatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SeatServiceImpl implements SeatService {
    private final SeatRepository seatRepository;
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final SeatMapper seatMapper;

    @Override
    public ServiceResult updateSeatStatus(UpdateSeatTypeRequest request) {
        List<Seat> seats = seatRepository.findByRoomIdAndPosition(
                request.getRoomId(),
                request.getFromRow(),
                request.getToRow(),
                request.getFromColumn(),
                request.getToColumn()
        );
        seats.forEach(seat -> seat.setType(request.getType()));
        return ServiceResult.ok();
    }

    @Override
    public ServiceResult getRoomSeats(Long roomId) {
        log.debug("getRoomSeats ({})", roomId);
        Optional<Room> roomOptional = roomRepository.findById(roomId);
        if (roomOptional.isEmpty()) {
            log.debug("Room not found: {}", roomId);
            return ServiceResult.fail(ErrorCode.ROOM_NOT_FOUND);
        }

        RoomSeatMapResponse response = new RoomSeatMapResponse();
        BeanUtils.copyProperties(roomOptional.get(), response);

        List<Seat> seats = seatRepository.findByRoomIdOrderByRowAndColumnAsc(roomId);
        response.setSeatMap(seatMapper.toListRoomSeatMapItem(seats));

        return ServiceResult.ok(response);
    }
}
