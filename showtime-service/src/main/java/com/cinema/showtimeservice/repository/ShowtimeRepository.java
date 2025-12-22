package com.cinema.showtimeservice.repository;

import com.cinema.showtimeservice.entity.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Long>, ShowtimeRepositoryCustom {

    @Query(value = """
            select r.id
            from rooms r
            where r.id not in
                  (select s.room_id from show_time s where s.start_time < ?2 and s.end_time > ?1)
    """, nativeQuery = true)
    List<Long> getFreeRoom(LocalDateTime startTime, LocalDateTime endTime);

    @Query(value = """
        select r.id
        from rooms r left join show_time s on r.id = s.room_id and CAST(s.start_time as date) = ?2
        WHERE r.id in ?1
        GROUP BY r.id
        ORDER BY count(s.id), r.id
        LIMIT 1
    """, nativeQuery = true)
    Optional<Long> getSuitableRoom(Collection<Long> roomIds, LocalDate date);

    @Query(value = """
        select 1 from show_time s where s.id <> ?1 and room_id = ?2 and s.start_time < ?4 and s.end_time > ?3
    """, nativeQuery = true)
    Optional<Integer> checkDuplicateTimeSlot(Long id, Long roomId, LocalDateTime startTime, LocalDateTime endTime);
}
