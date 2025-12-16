package com.cinema.movieservice.repository;

import com.cinema.movieservice.dto.response.PeopleResponse;
import com.cinema.movieservice.entity.MoviePeople;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MoviePeopleRepository extends JpaRepository<MoviePeople, Long> {
    @Query(value = "select new com.cinema.movieservice.dto.response.PeopleResponse(p.id, p.name, p.birthDate, p.role, p.avatarUrl) " +
            "from MoviePeople mp " +
            "join Person p on mp.peopleId = p.id " +
            "where mp.movieId = :movieId")
    List<PeopleResponse> findByMovieId(Long movieId);
}
