package com.cinema.security.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cinema.security.jwt")
@Getter
@Setter
public class JwtProperties {

    /**
     * claim chứa userId (vd: sub, user_id)
     */
    private String userIdClaim = "sub";
}