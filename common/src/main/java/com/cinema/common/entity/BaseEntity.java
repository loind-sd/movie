package com.cinema.common.entity;
import jakarta.persistence.*;
import java.time.*;
import lombok.*;

@MappedSuperclass @Getter @Setter
public class BaseEntity {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) Long id;
 LocalDateTime createdAt;
 LocalDateTime updatedAt;
}