package com.demo.hotel_reservation_api.domain.exception.Reservation;

import com.demo.hotel_reservation_api.domain.exception.DomainException;

public class InvalidReservationException extends DomainException {

    public InvalidReservationException(String message) {
        super(message);
    }
}