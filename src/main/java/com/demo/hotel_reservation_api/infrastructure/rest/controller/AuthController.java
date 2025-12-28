package com.demo.hotel_reservation_api.infrastructure.rest.controller;

import com.demo.hotel_reservation_api.application.dto.request.LoginRequest;
import com.demo.hotel_reservation_api.application.dto.request.RegisterRequest;
import com.demo.hotel_reservation_api.application.dto.response.AuthResponse;
import com.demo.hotel_reservation_api.application.usecase.auth.LoginUseCase;
import com.demo.hotel_reservation_api.application.usecase.auth.RegisterUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for authentication operations.
 * Exposes endpoints for user registration and login.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final RegisterUseCase registerUseCase;

    /**
     * Registers a new user account.
     *
     * @param request user registration data
     * @return authentication response with JWT token
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = registerUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Authenticates user and returns JWT token.
     *
     * @param request login credentials
     * @return authentication response with JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = loginUseCase.execute(request);
        return ResponseEntity.ok(response);
    }
}