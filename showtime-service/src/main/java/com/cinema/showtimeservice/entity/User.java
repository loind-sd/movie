package com.cinema.showtimeservice.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;


@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(unique = true, nullable = false)
    private String username;


    @Column(nullable = false)
    private String password; // encoded


    @Column(nullable = false)
    private String email;


    private String fullName;

    private String refreshToken;


    private Instant createdAt = Instant.now();
}
