package com.cinema.cinemaservice.service.impl;

import com.cinema.common.base.ServiceResult;
import com.cinema.common.enums.SeatStatus;
import com.cinema.common.enums.SeatType;
import com.cinema.common.exception.ErrorCode;
import com.cinema.cinemaservice.dto.request.room.RoomCreateRequest;
import com.cinema.cinemaservice.dto.request.room.RoomUpdateRequest;
import com.cinema.cinemaservice.dto.response.room.RoomDetailResponse;
import com.cinema.cinemaservice.entity.Room;
import com.cinema.cinemaservice.entity.Seat;
import com.cinema.cinemaservice.mapper.RoomMapper;
import com.cinema.cinemaservice.repository.RoomRepository;
import com.cinema.cinemaservice.repository.SeatRepository;
import com.cinema.cinemaservice.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final SeatRepository seatRepository;

    @Override
    public ServiceResult create(RoomCreateRequest request) {
        log.debug("create room request: {}", request);
        Room room = Room.builder()
                .name(request.getName())
                .totalRows(request.getTotalRows())
                .totalColumns(request.getTotalColumns())
                .build();

        roomRepository.save(room);
        List<Seat> seats = new ArrayList<>();
        for (int row = 1; row <= request.getTotalRows(); row++) {
            for (int col = 1; col <= request.getTotalColumns(); col++) {
                Seat seat = Seat.builder()
                        .room(room)
                        .rowIndex(row)
                        .columnIndex(col)
                        .type(SeatType.NORMAL)
                        .status(SeatStatus.AVAILABLE)
                        .label((char)('A' + row - 1) + String.valueOf(col))
                        .build();
                seats.add(seat);
            }
        }
        seatRepository.saveAll(seats);
        log.debug("create room successful: {}", room);
        return ServiceResult.ok();
    }

    @Override
    public ServiceResult update(RoomUpdateRequest request) {
        log.debug("update room request: {}", request);
        Optional<Room> optionalRoom = roomRepository.findById(request.getId());
        if (optionalRoom.isPresent()) {
            Room room = optionalRoom.get();
            room.setName(request.getName());
            roomRepository.save(room);
            log.debug("update room successful: {}", room);
            return ServiceResult.ok();
        }
        return ServiceResult.fail(ErrorCode.ROOM_NOT_FOUND);
    }

    @Override
    public ServiceResult getDetail(Long roomId) {
        log.debug("get room detail: {}", roomId);
        Optional<Room> optionalRoom = roomRepository.findById(roomId);
        if (optionalRoom.isPresent()) {
            Room room = optionalRoom.get();
            RoomDetailResponse response = roomMapper.toRoomDetailResponse(room);
            return ServiceResult.ok(response);
        }
        return ServiceResult.fail(ErrorCode.ROOM_NOT_FOUND);
    }

    @Override
    public ServiceResult delete(Long roomId) {
        log.debug("delete room: {}", roomId);
        Optional<Room> optionalRoom = roomRepository.findById(roomId);
        if (optionalRoom.isPresent()) {
            roomRepository.deleteById(roomId);
            log.debug("delete room successful: {}", roomId);
            return ServiceResult.ok();
        }
        return ServiceResult.fail(ErrorCode.ROOM_NOT_FOUND);
    }
}
