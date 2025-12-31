package com.demo.hotel_reservation_api.infrastructure.rest.controller;

import com.demo.hotel_reservation_api.application.dto.request.CreateReservationRequest;
import com.demo.hotel_reservation_api.application.usecase.reservation.*;
import com.demo.hotel_reservation_api.domain.model.enums.ReservationStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for reservation operations.
 */
@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@Tag(name = "Reservations", description = "Hotel room reservation management")
@SecurityRequirement(name = "JWT")
public class ReservationController {

    private final CreateReservationUseCase createReservationUseCase;
    private final CancelReservationUseCase cancelReservationUseCase;
    private final ConfirmReservationUseCase confirmReservationUseCase;
    private final GetReservationUseCase getReservationUseCase;
    private final GetAllReservationsUseCase getAllReservationUseCase;
    private final GetUserReservationsUseCase getUserReservationUseCase;

    @Operation(summary = "Create new reservation", description = "Creates a reservation for authenticated client")
    @PostMapping
    public ResponseEntity<?> createReservation(
            @Valid @RequestBody CreateReservationRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        var response = createReservationUseCase.execute(request, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Confirm reservation", description = "Confirms a pending reservation (admin only)")
    @PutMapping("/{reservationId}/confirm")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> confirmReservation(
            @PathVariable Long reservationId) {

        var response = confirmReservationUseCase.execute(reservationId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Cancel reservation", description = "Cancels a reservation if allowed")
    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Void> cancelReservation(
            @PathVariable Long reservationId,
            @AuthenticationPrincipal UserDetails userDetails) {

        cancelReservationUseCase.execute(reservationId, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get my reservations", description = "Returns all reservations for authenticated user")
    @GetMapping("/my-reservations")
    public ResponseEntity<?> getMyReservations(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) ReservationStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        var response = getUserReservationUseCase.execute(
                userDetails.getUsername(),
                status,
                page,
                size
        );
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all reservations", description = "Returns all reservations (admin only)")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllReservations(
            @RequestParam(required = false) Long roomId,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) ReservationStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        var response = getAllReservationUseCase.execute(roomId, userId, status, page, size);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get reservation by ID", description = "Returns a specific reservation (owner or admin only)")
    @GetMapping("/{reservationId}")
    public ResponseEntity<?> getReservationById(
            @PathVariable Long reservationId,
            @AuthenticationPrincipal UserDetails userDetails) {

        var reservation = getReservationUseCase.execute(reservationId, userDetails.getUsername());
        return ResponseEntity.ok(reservation);
    }
}