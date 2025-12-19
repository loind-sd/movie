package com.cinema.cinemaservice.repository;

import com.cinema.cinemaservice.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    @Query("""
        SELECT s FROM Seat s
        WHERE s.room.id = :roomId
        AND s.rowIndex between :fromRow AND :toRow
        AND s.columnIndex between :fromColumn AND :toColumn
    """)
    List<Seat> findByRoomIdAndPosition(
            @Param("roomId") Long roomId,
            @Param("fromRow") Integer fromRow,
            @Param("toRow") Integer toRow,
            @Param("fromColumn") Integer fromColumn,
            @Param("toColumn") Integer toColumn
    );

    @Query("""
        SELECT s FROM Seat s
        WHERE s.room.id = :roomId
        ORDER BY s.rowIndex ASC, s.columnIndex ASC
    """)
    List<Seat> findByRoomIdOrderByRowAndColumnAsc(Long roomId);
}
