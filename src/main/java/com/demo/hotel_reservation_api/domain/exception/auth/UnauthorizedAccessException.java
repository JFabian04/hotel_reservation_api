package com.demo.hotel_reservation_api.domain.exception.auth;

import com.demo.hotel_reservation_api.domain.exception.DomainException;

public class UnauthorizedAccessException extends DomainException {

    public UnauthorizedAccessException(String message) {
        super(message);
    }

    public UnauthorizedAccessException() {
        super("You don't have permission to access this resource");
    }
}