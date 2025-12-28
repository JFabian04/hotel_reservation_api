package com.demo.hotel_reservation_api.infrastructure.security;

import com.demo.hotel_reservation_api.application.port.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Adapter implementing PasswordEncoder port using Spring Security's BCrypt.
 * Bridges application port with infrastructure implementation.
 */
@Component
@RequiredArgsConstructor
public class BCryptPasswordEncoderAdapter implements PasswordEncoder {

    private final org.springframework.security.crypto.password.PasswordEncoder springEncoder;

    @Override
    public String encode(String rawPassword) {
        return springEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return springEncoder.matches(rawPassword, encodedPassword);
    }
}