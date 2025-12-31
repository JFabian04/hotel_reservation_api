package com.demo.hotel_reservation_api.domain.exception.Reservation;

import com.demo.hotel_reservation_api.domain.exception.DomainException;

public class ReservationNotFoundException extends DomainException {

    public ReservationNotFoundException(Long id) {
        super("Reservation not found with id: " + id);
    }
}