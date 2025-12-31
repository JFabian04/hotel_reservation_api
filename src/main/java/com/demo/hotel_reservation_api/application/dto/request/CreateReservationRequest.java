package com.demo.hotel_reservation_api.application.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record CreateReservationRequest(
        @NotNull(message = "Room ID is required")
        @JsonProperty("room_id")
        Long roomId,

        @NotNull(message = "Check-in date is required")
        @Future(message = "Check-in date must be in the future")
        @JsonProperty("check_in")
        LocalDate checkIn,

        @NotNull(message = "Check-out date is required")
        @Future(message = "Check-out date must be in the future")
        @JsonProperty("check_out")
        LocalDate checkOut,

        @NotNull(message = "Guest count is required")
        @Min(value = 1, message = "Guest count must be at least 1")
        @Max(value = 10, message = "Guest count cannot exceed 10")
        @JsonProperty("guest_count")
        Integer guestCount
) { }
