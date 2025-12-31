package com.demo.hotel_reservation_api.domain.exception.room;

import com.demo.hotel_reservation_api.domain.exception.DomainException;

public class RoomNotFoundException extends DomainException {

    public RoomNotFoundException(Long id) {
        super("Room not found with id: " + id);
    }

    public RoomNotFoundException(String number) {
        super("Room not found with number: " + number);
    }
}
