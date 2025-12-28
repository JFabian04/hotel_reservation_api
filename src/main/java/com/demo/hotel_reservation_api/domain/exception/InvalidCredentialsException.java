package com.demo.hotel_reservation_api.domain.exception;

/**
 * Exception thrown when user authentication fails due to invalid credentials.
 * Uses generic message to prevent user enumeration attacks.
 */
public class InvalidCredentialsException extends DomainException {
    public InvalidCredentialsException() {
        super("Invalid credentials");
    }
}