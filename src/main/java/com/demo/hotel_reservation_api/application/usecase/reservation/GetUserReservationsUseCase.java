package com.demo.hotel_reservation_api.application.usecase.reservation;

import com.demo.hotel_reservation_api.application.dto.response.PagedReservationResponse;
import com.demo.hotel_reservation_api.application.dto.response.ReservationResponse;
import com.demo.hotel_reservation_api.domain.exception.auth.UserNotFoundException;
import com.demo.hotel_reservation_api.domain.model.Reservation;
import com.demo.hotel_reservation_api.domain.model.User;
import com.demo.hotel_reservation_api.domain.model.enums.ReservationStatus;
import com.demo.hotel_reservation_api.domain.repository.ReservationRepository;
import com.demo.hotel_reservation_api.domain.repository.ReservationRepository.PageResult;
import com.demo.hotel_reservation_api.domain.repository.UserRepository;
import com.demo.hotel_reservation_api.domain.valueobject.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetUserReservationsUseCase {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public PagedReservationResponse execute(String userEmail, ReservationStatus status, int page, int size) {

        validatePaginationParams(page, size);

        Email email = new Email(userEmail);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(userEmail));

        PageResult<Reservation> pageResult = reservationRepository.findByUserId(
                user.getId(),
                status,
                page,
                size
        );

        List<ReservationResponse> reservationResponses = pageResult.content().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return new PagedReservationResponse(
                reservationResponses,
                pageResult.pageNumber(),
                pageResult.pageSize(),
                pageResult.totalElements(),
                pageResult.totalPages(),
                pageResult.hasNext(),
                pageResult.hasPrevious()
        );
    }

    private ReservationResponse mapToResponse(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                new ReservationResponse.RoomSummary(
                        reservation.getRoom().getId(),
                        reservation.getRoom().getNumber(),
                        reservation.getRoom().getType().name()
                ),
                new ReservationResponse.UserSummary(
                        reservation.getUser().getId(),
                        reservation.getUser().getEmail().value()
                ),
                reservation.getCheckInDate(),
                reservation.getCheckOutDate(),
                reservation.getGuestCount(),
                reservation.getTotalAmount(),
                reservation.getStatus().name(),
                reservation.getNumberOfNights()
        );
    }

    private void validatePaginationParams(int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("Page number cannot be negative");
        }
        if (size <= 0 || size > 100) {
            throw new IllegalArgumentException("Page size must be between 1 and 100");
        }
    }
}
