package com.demo.hotel_reservation_api.application.usecase.auth;

import com.demo.hotel_reservation_api.application.dto.request.LoginRequest;
import com.demo.hotel_reservation_api.application.dto.response.AuthResponse;
import com.demo.hotel_reservation_api.application.dto.response.UserResponse;
import com.demo.hotel_reservation_api.application.port.PasswordEncoder;
import com.demo.hotel_reservation_api.application.port.TokenProvider;
import com.demo.hotel_reservation_api.domain.exception.auth.InvalidCredentialsException;
import com.demo.hotel_reservation_api.domain.model.User;
import com.demo.hotel_reservation_api.domain.repository.UserRepository;
import com.demo.hotel_reservation_api.domain.valueobject.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Authenticates users and provides JWT tokens.
 * Follows Clean Architecture - application layer coordinates domain objects.
 */
@Service
@RequiredArgsConstructor
public class LoginUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Transactional(readOnly = true)
    public AuthResponse execute(LoginRequest request) {

        Email email = new Email(request.email());
        User user = userRepository.findByEmail(email)
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }
        String token = tokenProvider.generateToken(user);

        UserResponse userResponse = new UserResponse(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail().value(),
                user.getRole().name()
        );

        return new AuthResponse(token, userResponse);
    }
}