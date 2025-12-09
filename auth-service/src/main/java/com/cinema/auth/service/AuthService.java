package com.cinema.auth.service;

import com.cinema.auth.dto.AuthRequest;
import com.cinema.auth.dto.JwtResponse;
import com.cinema.auth.dto.RegisterRequest;
import com.cinema.auth.entity.User;
import com.cinema.auth.repository.UserRepository;
import com.cinema.auth.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;



    public JwtResponse login(AuthRequest req) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.username(), req.password()));
        User user = userRepository.findByUsername(req.username()).orElseThrow();
        String accessToken = jwtUtil.generateAccessToken(user.getUsername());
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


        String accessToken = jwtUtil.generateAccessToken(u.getUsername());
        return new JwtResponse(accessToken, accessToken, "Bearer");
    }
}
