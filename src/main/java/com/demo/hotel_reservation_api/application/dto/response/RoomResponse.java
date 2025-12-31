package com.demo.hotel_reservation_api.application.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public record RoomResponse(
        @JsonProperty("id")
        Long id,

        @JsonProperty("number")
        String number,

        @JsonProperty("type")
        String type,

        @JsonProperty("price_per_night")
        BigDecimal pricePerNight,

        @JsonProperty("status")
        String status,

        @JsonProperty("is_available")
        Boolean isAvailable
) {}