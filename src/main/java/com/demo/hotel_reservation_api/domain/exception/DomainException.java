package com.demo.hotel_reservation_api.domain.exception;

/**
 * Base exception for domain-specific business rule violations.
 * All custom business exceptions should extend this class.
 */
public class DomainException extends RuntimeException {
    /**
     * @param message Description of the business rule violation
     */
    protected DomainException(String message) {
        super(message);
    }

    /**
     * @param message Description of the business rule violation
     * @param cause Underlying exception that triggered this business error
     */
    protected DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
