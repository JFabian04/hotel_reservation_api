package com.demo.hotel_reservation_api.application.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record RegisterRequest(

        @NotBlank(message = "firstName is required")
        @Size(min = 3, max = 50, message = "firstName must be between 3 and 50 characters")
        String firstName,

        @NotBlank(message = "lastName is required")
        @Size(min = 3, max = 50, message = "lastName must be between 3 and 50 characters")
        String lastName,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must be at least 6 characters")
        String password
) {}