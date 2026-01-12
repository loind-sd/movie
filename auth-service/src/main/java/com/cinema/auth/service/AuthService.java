package com.cinema.auth.service;

import com.cinema.auth.dto.AuthRequest;
import com.cinema.auth.dto.JwtResponse;
import com.cinema.auth.dto.RegisterRequest;
import com.cinema.auth.entity.User;
import com.cinema.auth.repository.UserRepository;
import com.cinema.common.constants.CommonConstants;
import com.cinema.common.dto.RefreshTokenRequest;
import com.cinema.common.exception.BadRequestException;
import com.cinema.common.exception.BusinessException;
import com.cinema.common.exception.ErrorCode;
import com.cinema.common.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RedisLoginAttemptService loginAttemptService;
    private final RedisService redisService;

    @Value("${jwt.access-token-expire}")
    private Integer accessTokenExpire;

    public JwtResponse login(AuthRequest request) {
        String username = request.username();

        // Check block
        if (loginAttemptService.isBlocked(username)) {
            throw new BusinessException(ErrorCode.ACCOUNT_LOCKED_TOO_MANY_ATTEMPTS);
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // Validate password
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            loginAttemptService.loginFailed(username);
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
        }

        // Login thành công
        loginAttemptService.loginSucceeded(username);

        Pair<String, String> tokenPair = jwtService.generateToken(String.valueOf(user.getId()));
        redisService.setValueWithExpireTime(
                String.format(CommonConstants.RedisKey.TOKEN_KEY, user.getId()),
                tokenPair.getLeft(),
                accessTokenExpire,
                TimeUnit.MICROSECONDS
        );
        user.setRefreshToken(tokenPair.getRight());
        userRepository.save(user);
        return new JwtResponse(user.getId(), tokenPair.getLeft(), tokenPair.getRight(), "Bearer");
    }


    public JwtResponse register(RegisterRequest req) {
        if (userRepository.existsByUsername(req.username())) {
            throw new BadRequestException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        User u = new User();
        u.setUsername(req.username());
        u.setPassword(passwordEncoder.encode(req.password()));
        u.setEmail(req.email());
        u.setFullName(req.fullName());
        userRepository.save(u);

        Pair<String, String> tokenPair = jwtService.generateToken(String.valueOf(u.getId()));
        u.setRefreshToken(tokenPair.getRight());
        userRepository.save(u);
        return new JwtResponse(u.getId(), tokenPair.getLeft(), tokenPair.getRight(), "Bearer");
    }

    public JwtResponse refresh(RefreshTokenRequest req) {
        User user = userRepository.findById(req.userId())
                .orElseThrow(() -> new BadRequestException(ErrorCode.USER_NOT_FOUND));

        if (!Objects.equals(req.callInsideService(), Boolean.TRUE)
                && !Objects.equals(user.getRefreshToken(), req.refreshToken())) {
            throw new BadRequestException(ErrorCode.REFRESH_TOKEN_INVALID);
        }

        Pair<String, String> tokenPair = jwtService.generateToken(String.valueOf(user.getId()));
        redisService.setValueWithExpireTime(
                String.format(CommonConstants.RedisKey.TOKEN_KEY, user.getId()),
                tokenPair.getLeft(),
                accessTokenExpire,
                TimeUnit.MICROSECONDS
        );
        user.setRefreshToken(tokenPair.getRight());
        return new JwtResponse(user.getId(), tokenPair.getLeft(), tokenPair.getRight(), "Bearer");
    }
}
