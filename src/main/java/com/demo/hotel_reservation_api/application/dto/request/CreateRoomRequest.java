package com.demo.hotel_reservation_api.application.dto.request;

import com.demo.hotel_reservation_api.domain.model.enums.RoomType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CreateRoomRequest(

        @NotBlank(message = "Room number is required")
        @Size(max = 10, message = "Room number cannot exceed 10 characters")
        String number,

        @NotNull(message = "Room type is required")
        RoomType type,

        @NotNull(message = "Price per night is required")
        @DecimalMin(value = "0.01", message = "Price must be greater than 0")
        @Digits(integer = 8, fraction = 2, message = "Price format invalid")
        BigDecimal pricePerNight
) {}