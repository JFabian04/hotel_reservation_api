package com.demo.hotel_reservation_api.domain.model.enums;

public enum RoomType {
    SINGLE(1),
    DOUBLE(2),
    TWIN(2),
    SUITE(4);

    private final int maxGuests;

    RoomType(int maxGuests) {
        this.maxGuests = maxGuests;
    }

    /**
     * Returns maximum number of guests allowed for this room type.
     */
    public int getMaxGuests() {
        return maxGuests;
    }
}