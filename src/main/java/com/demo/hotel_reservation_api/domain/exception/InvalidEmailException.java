package com.demo.hotel_reservation_api.domain.exception;

/**
 * Thrown when an email fails format validation.
 * Typically used during user registration or email updates.
 */
public class InvalidEmailException extends DomainException {
    public InvalidEmailException(String message) {
        super(message);
    }
}
