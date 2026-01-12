package com.cinema.showtimeservice.repository;

import com.cinema.showtimeservice.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByRoomId(Long roomId);

    @Query(value = """
        select bs.seat_id
        from booking b join booking_seat bs on b.id = bs.booking_id
        where showtime_id = ?1
    """, nativeQuery = true)
    List<Long> findBookingSeat(Long showtimeId);
}
