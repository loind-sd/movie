package com.cinema.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import java.util.Date;

@MappedSuperclass
@Getter
@Setter
public class BaseEntity {

    @CreatedBy
    @Column(name = "create_user")
    private Integer createUser;

    @CreationTimestamp
    @Column(name = "create_time")
    private Date createTime;

    @LastModifiedBy
    @Column(name = "update_user")
    private Integer updateUser;

    @UpdateTimestamp
    @Column(name = "update_time")
    private Date updateTime;
}