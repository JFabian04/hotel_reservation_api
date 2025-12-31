package com.demo.hotel_reservation_api.application.usecase.auth;

import com.demo.hotel_reservation_api.application.dto.request.RegisterRequest;
import com.demo.hotel_reservation_api.application.dto.response.AuthResponse;
import com.demo.hotel_reservation_api.application.dto.response.UserResponse;
import com.demo.hotel_reservation_api.application.port.PasswordEncoder;
import com.demo.hotel_reservation_api.application.port.TokenProvider;
import com.demo.hotel_reservation_api.domain.model.User;
import com.demo.hotel_reservation_api.domain.model.enums.UserRole;
import com.demo.hotel_reservation_api.domain.repository.UserRepository;
import com.demo.hotel_reservation_api.domain.valueobject.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Registers new users in the system.
 * Enforces business rules: unique email, password hashing, default client role.
 */
@Service
@RequiredArgsConstructor
public class RegisterUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Transactional
    public AuthResponse execute(RegisterRequest request) {

        Email email = new Email(request.email());

        String hashedPassword = passwordEncoder.encode(request.password());

        User user = new User(
                request.firstName(),
                request.lastName(),
                email,
                hashedPassword,
                UserRole.CLIENT
        );

        User savedUser = userRepository.save(user);
        String token = tokenProvider.generateToken(savedUser);

        UserResponse userResponse = new UserResponse(
                savedUser.getId(),
                savedUser.getFirstname(),
                savedUser.getLastname(),
                savedUser.getEmail().value(),
                savedUser.getRole().name()
        );

        return new AuthResponse(token, userResponse);
    }
}