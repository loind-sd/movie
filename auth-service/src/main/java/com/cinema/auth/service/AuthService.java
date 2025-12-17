package com.cinema.auth.service;

import com.cinema.auth.dto.AuthRequest;
import com.cinema.auth.dto.JwtResponse;
import com.cinema.auth.dto.RegisterRequest;
import com.cinema.auth.entity.User;
import com.cinema.auth.repository.UserRepository;
import com.cinema.common.exception.BusinessException;
import com.cinema.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RedisLoginAttemptService loginAttemptService;

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

        String accessToken = jwtService.generateToken(String.valueOf(user.getId()));
        return new JwtResponse(accessToken, accessToken, "Bearer");
    }


    public JwtResponse register(RegisterRequest req) {
        if (userRepository.existsByUsername(req.username())) {
            throw new IllegalArgumentException("username already exists");
        }
        User u = new User();
        u.setUsername(req.username());
        u.setPassword(passwordEncoder.encode(req.password()));
        u.setEmail(req.email());
        u.setFullName(req.fullName());
        userRepository.save(u);


        String accessToken = jwtService.generateToken(String.valueOf(u.getId()));
        return new JwtResponse(accessToken, accessToken, "Bearer");
    }
}
