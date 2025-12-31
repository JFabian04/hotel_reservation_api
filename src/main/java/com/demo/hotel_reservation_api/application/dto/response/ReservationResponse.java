package com.demo.hotel_reservation_api.application.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDate;

public record  ReservationResponse(
        @JsonProperty("id")
        Long id,

        @JsonProperty("room")
        RoomSummary room,

        @JsonProperty("user")
        UserSummary user,

        @JsonProperty("check_in")
        LocalDate checkIn,

        @JsonProperty("check_out")
        LocalDate checkOut,

        @JsonProperty("guest_count")
        Integer guestCount,

        @JsonProperty("total_amount")
        BigDecimal totalAmount,

        @JsonProperty("status")
        String status,

        @JsonProperty("number_of_nights")
        Long numberOfNights
) {
    public record RoomSummary(
            Long id,
            String number,
            String type
    ) {}

    public record UserSummary(
            Long id,
            String email
    ) {}
}
