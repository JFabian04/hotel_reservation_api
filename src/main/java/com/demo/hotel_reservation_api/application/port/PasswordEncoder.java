package com.demo.hotel_reservation_api.application.port;

/**
 * Defines password encoding contract.
 * Implementation provided by infrastructure layer (BCrypt, etc).
 */
public interface PasswordEncoder {
    String encode(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
}