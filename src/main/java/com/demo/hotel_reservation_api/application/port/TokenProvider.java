package com.demo.hotel_reservation_api.application.port;

import com.demo.hotel_reservation_api.domain.model.User;

/**
 * Contract for JWT token generation and validation.
 * Infrastructure implementations handle token creation and security.
 */
public interface TokenProvider {
    String generateToken(User user);
    String validateTokenAndGetUsername(String token);

    boolean isTokenValid(String token);
}