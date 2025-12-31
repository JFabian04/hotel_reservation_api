package com.demo.hotel_reservation_api.domain.model;

import com.demo.hotel_reservation_api.domain.model.enums.RoomStatus;
import com.demo.hotel_reservation_api.domain.model.enums.RoomType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Room {
    private Long id;
    private String number;
    private RoomType type;
    private BigDecimal pricePerNight;
    private RoomStatus status;

    public Room(String number, RoomType type, BigDecimal pricePerNight) {
        validate(number, pricePerNight);
        this.number = number;
        this.type = type;
        this.pricePerNight = pricePerNight;
        this.status = RoomStatus.AVAILABLE;
    }

    public Room(Long id, String number, RoomType type,
                BigDecimal pricePerNight, RoomStatus status) {
        this.id = id;
        this.number = number;
        this.type = type;
        this.pricePerNight = pricePerNight;
        this.status = status;
    }

    private void validate(String number, BigDecimal pricePerNight) {
        if (number == null || number.isBlank()) {
            throw new IllegalArgumentException("Room number cannot be empty");
        }
        if (pricePerNight == null || pricePerNight.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
    }

    public boolean isAvailable() {
        return RoomStatus.AVAILABLE.equals(status);
    }

    public void markAsMaintenance() {
        this.status = RoomStatus.MAINTENANCE;
    }

    public void markAsAvailable() {
        this.status = RoomStatus.AVAILABLE;
    }

    public void markAsOccupied() {
        this.status = RoomStatus.OCCUPIED;
    }
}
