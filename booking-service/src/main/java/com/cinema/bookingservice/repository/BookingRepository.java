package com.cinema.bookingservice.repository;

import com.cinema.bookingservice.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findByUserIdAndShowtimeIdAndStatus(Long userId, Long showtimeId, String status);

    @Query(value = """
            select 1
            from booking b join booking_seat bs on b.id = bs.booking_id
            where b.showtime_id = ?1 and user_id <> ?2 and seat_id in ?3
    """, nativeQuery = true)
    Optional<Integer> checkSeatAlreadyBooked(Long showtimeId, Long userId, Collection<Long> seatIds);
}
