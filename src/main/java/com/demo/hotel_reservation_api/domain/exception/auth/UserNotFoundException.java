package com.demo.hotel_reservation_api.domain.exception.auth;

import com.demo.hotel_reservation_api.domain.exception.DomainException;

/**
 * Thrown when a requested user cannot be found in the system.
 * Used in user lookup operations.
 */
public class UserNotFoundException extends DomainException {

    public UserNotFoundException(String identifier) {
        super("User not found: " + identifier);
    }
}
