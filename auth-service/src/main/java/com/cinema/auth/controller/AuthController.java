package com.cinema.auth.controller;

import com.cinema.auth.dto.AuthRequest;
import com.cinema.auth.dto.JwtResponse;
import com.cinema.auth.dto.RegisterRequest;
import com.cinema.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {


    private final AuthService authService;


    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody @Validated AuthRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }


    @PostMapping("/register")
    public ResponseEntity<JwtResponse> register(@RequestBody @Validated RegisterRequest req) {
        return ResponseEntity.ok(authService.register(req));
    }


//    @PostMapping("/refresh")
//    public ResponseEntity<JwtResponse> refresh(@RequestParam("refreshToken") String refreshToken) {
//        return ResponseEntity.ok(authService.refreshToken(refreshToken));
//    }
}
