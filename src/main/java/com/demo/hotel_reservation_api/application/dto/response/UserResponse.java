package com.demo.hotel_reservation_api.application.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserResponse(

        @JsonProperty("id")
        Long id,

        @JsonProperty("firstName")
        String firstName,

        @JsonProperty("lastName")
        String lastName,

        @JsonProperty("email")
        String email,

        @JsonProperty("role")
        String role
) {}