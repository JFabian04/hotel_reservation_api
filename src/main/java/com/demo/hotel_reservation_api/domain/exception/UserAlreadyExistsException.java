package com.demo.hotel_reservation_api.domain.exception;

/**
 * Thrown when attempting to register a user with an existing email/username.
 * Prevents duplicate accounts in the system.
 */
public class UserAlreadyExistsException  extends DomainException {

    public UserAlreadyExistsException(String identifier) {
        super("User already exists: " + identifier);
    }
}