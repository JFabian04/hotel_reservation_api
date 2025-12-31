package com.demo.hotel_reservation_api.application.dto.request;

import com.demo.hotel_reservation_api.domain.model.enums.RoomStatus;
import com.demo.hotel_reservation_api.domain.model.enums.RoomType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;

import java.math.BigDecimal;

public record UpdateRoomRequest(
        RoomType type,
        @DecimalMin(value = "0.01", message = "Price must be greater than 0")
        @Digits(integer = 8, fraction = 2, message = "Price format invalid")
        BigDecimal pricePerNight,
        RoomStatus status)
{ }
