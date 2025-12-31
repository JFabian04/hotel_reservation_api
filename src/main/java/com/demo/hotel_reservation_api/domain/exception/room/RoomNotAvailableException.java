package com.demo.hotel_reservation_api.domain.exception.room;

import com.demo.hotel_reservation_api.domain.exception.DomainException;

public class RoomNotAvailableException extends DomainException {

    public RoomNotAvailableException(String roomNumber, String dates) {
        super("Room " + roomNumber + " is not available for dates: " + dates);
    }

    public RoomNotAvailableException(String message) {
        super(message);
    }
}