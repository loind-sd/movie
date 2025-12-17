package com.cinema.auth.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@MappedSuperclass @Getter @Setter
public class BaseEntity {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) Long id;
 LocalDateTime createdAt; LocalDateTime updatedAt;
}