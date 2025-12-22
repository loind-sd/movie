package com.cinema.showtimeservice.repository;

import com.cinema.showtimeservice.entity.ShowtimePrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowtimePriceRepository extends JpaRepository<ShowtimePrice, Long> {
    List<ShowtimePrice> findByShowtimeId(Long showtimeId);

    void deleteByShowtimeId(Long showtimeId);
}
