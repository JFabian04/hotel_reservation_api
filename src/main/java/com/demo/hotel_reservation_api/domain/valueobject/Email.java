package com.demo.hotel_reservation_api.domain.valueobject;

import com.demo.hotel_reservation_api.domain.exception.auth.InvalidEmailException;
import java.util.regex.Pattern;

/**
 * Email value object ensuring valid email format and normalization.
 * @throws InvalidEmailException if email format is invalid
 */
public record Email(String value) {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    /**
     * Creates a validated Email instance.
     *
     * @param value the email address string
     * @throws IllegalArgumentException if email format is invalid
     */
    public Email {
        if (value == null || value.isBlank()) {
            throw new InvalidEmailException("Email cannot be empty");
        }

        String trimmed = value.trim().toLowerCase();

        if (!EMAIL_PATTERN.matcher(trimmed).matches()) {
            throw new InvalidEmailException("Invalid email format: " + value);
        }

        value = trimmed;
    }

    @Override
    public String toString() {
        return value;
    }
}