package com.demo.hotel_reservation_api.domain.model;

import com.demo.hotel_reservation_api.domain.exception.Reservation.InvalidReservationException;
import com.demo.hotel_reservation_api.domain.exception.Reservation.InvalidStateTransitionException;
import com.demo.hotel_reservation_api.domain.model.enums.ReservationStatus;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Getter
public class Reservation {
    private Long id;
    private Room room;
    private User user;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer guestCount;
    private BigDecimal totalAmount;
    private ReservationStatus status;

    public Reservation(Room room, User user, LocalDate checkInDate,
                       LocalDate checkOutDate, Integer guestCount) {

        validateDates(checkInDate, checkOutDate);
        validateRoom(room, checkInDate, checkOutDate);
        validateGuestCount(guestCount);

        this.room = room;
        this.user = user;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.guestCount = guestCount;
        this.status = ReservationStatus.PENDING;

        this.totalAmount = calculateTotalAmount(room.getPricePerNight(), checkInDate, checkOutDate);
    }

    public Reservation(Long id, Room room, User user, LocalDate checkInDate,
                       LocalDate checkOutDate, Integer guestCount,
                       BigDecimal totalAmount, ReservationStatus status) {
        this.id = id;
        this.room = room;
        this.user = user;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.guestCount = guestCount;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public void confirm() {
        if (status != ReservationStatus.PENDING) {
            throw new InvalidStateTransitionException(
                    "Can only confirm reservations in PENDING status. Current status: " + status
            );
        }
        this.status = ReservationStatus.CONFIRMED;
    }

    public void cancel() {
        if (status == ReservationStatus.CANCELLED) {
            throw new InvalidStateTransitionException("Reservation is already cancelled");
        }
        if (status == ReservationStatus.COMPLETED) {
            throw new InvalidStateTransitionException("Cannot cancel a completed reservation");
        }
        this.status = ReservationStatus.CANCELLED;
    }

    public void complete() {
        if (status != ReservationStatus.CONFIRMED) {
            throw new InvalidStateTransitionException(
                    "Can only complete reservations in CONFIRMED status. Current status: " + status
            );
        }
        this.status = ReservationStatus.COMPLETED;
    }

    public boolean canBeCancelledByUser() {
        if (status == ReservationStatus.CANCELLED || status == ReservationStatus.COMPLETED) {
            return false;
        }

        LocalDate now = LocalDate.now();
        return checkInDate.isAfter(now);
    }

    public long getNumberOfNights() {
        return ChronoUnit.DAYS.between(checkInDate, checkOutDate);
    }

    public boolean overlapsWith(LocalDate otherCheckIn, LocalDate otherCheckOut) {
        return !this.checkOutDate.isBefore(otherCheckIn)
                && !otherCheckOut.isBefore(this.checkInDate);
    }

    public boolean isActive() {
        return status != ReservationStatus.CANCELLED;
    }

    // ========== PRIVATE VALIDATIONS ==========
    private void validateDates(LocalDate checkIn, LocalDate checkOut) {
        if (checkIn == null || checkOut == null) {
            throw new InvalidReservationException("Check-in and check-out dates are required");
        }

        LocalDate today = LocalDate.now();
        if (checkIn.isBefore(today)) {
            throw new InvalidReservationException("Check-in date cannot be in the past");
        }

        if (checkOut.isBefore(checkIn) || checkOut.isEqual(checkIn)) {
            throw new InvalidReservationException("Check-out date must be after check-in date");
        }

        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        if (nights > 30) {
            throw new InvalidReservationException("Reservation cannot exceed 30 nights");
        }
    }

    private void validateRoom(Room room, LocalDate checkIn, LocalDate checkOut) {
        if (room == null) {
            throw new InvalidReservationException("Room is required");
        }

        if (!room.isAvailable()) {
            throw new InvalidReservationException(
                    "Room " + room.getNumber() + " is not available for reservation. Status: " + room.getStatus()
            );
        }
    }

    private void validateGuestCount(Integer guestCount) {
        if (guestCount == null || guestCount < 1) {
            throw new InvalidReservationException("Guest count must be at least 1");
        }
        if (guestCount > 10) {
            throw new InvalidReservationException("Guest count cannot exceed 10");
        }
    }

    private BigDecimal calculateTotalAmount(BigDecimal pricePerNight, LocalDate checkIn, LocalDate checkOut) {
        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        return pricePerNight.multiply(BigDecimal.valueOf(nights));
    }
}