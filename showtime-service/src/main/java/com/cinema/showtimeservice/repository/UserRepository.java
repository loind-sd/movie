package com.cinema.showtimeservice.repository;

import com.cinema.showtimeservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
