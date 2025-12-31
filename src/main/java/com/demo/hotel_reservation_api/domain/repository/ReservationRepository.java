package com.demo.hotel_reservation_api.domain.repository;

import com.demo.hotel_reservation_api.domain.model.Reservation;
import com.demo.hotel_reservation_api.domain.model.enums.ReservationStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {
    Reservation save(Reservation reservation);
    Optional<Reservation> findById(Long id);
    PageResult<Reservation> findByUserId(Long userId, ReservationStatus status, int page, int size);
    PageResult<Reservation> findAll(Long roomId, Long userId, ReservationStatus status, int page, int size);
    boolean existsConflictingReservation(Long roomId, LocalDate checkIn, LocalDate checkOut, Long excludeReservationId);
    List<Reservation> findActiveReservationsByRoomAndDateRange(Long roomId, LocalDate checkIn, LocalDate checkOut);
    record PageResult<T>(
            List<T> content,
            int pageNumber,
            int pageSize,
            long totalElements,
            int totalPages,
            boolean hasNext,
            boolean hasPrevious
    ) {}
}
