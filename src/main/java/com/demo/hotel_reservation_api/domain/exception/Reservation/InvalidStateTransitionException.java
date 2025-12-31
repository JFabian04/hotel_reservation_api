package com.demo.hotel_reservation_api.domain.exception.Reservation;

import com.demo.hotel_reservation_api.domain.exception.DomainException;

public class InvalidStateTransitionException extends DomainException {

    public InvalidStateTransitionException(String message) {
        super(message);
    }
}