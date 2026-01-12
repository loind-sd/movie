package com.cinema.bookingservice.repository;

import com.cinema.bookingservice.entity.BookingSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingSeatRepository extends JpaRepository<BookingSeat, Long> {
    void deleteByBookingId(long bookingId);
}
