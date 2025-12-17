package com.cinema.movieservice.entity;

import com.cinema.common.entity.BaseEntity;
import com.cinema.common.enums.PersonRole;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "persons")
@Data
public class Person extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column
    @Enumerated(EnumType.STRING)
    private PersonRole role; // ACTOR, DIRECTOR

    @Column
    private String avatarUrl;
}

