package com.demo.hotel_reservation_api.domain.exception.room;

import com.demo.hotel_reservation_api.domain.exception.DomainException;

public class RoomAlreadyExistsException extends DomainException {

    public RoomAlreadyExistsException(String number) {
        super("Room already exists with number: " + number);
    }
}