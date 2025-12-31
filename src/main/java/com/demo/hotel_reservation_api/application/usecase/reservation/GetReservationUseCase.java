package com.demo.hotel_reservation_api.application.usecase.reservation;

import com.demo.hotel_reservation_api.application.dto.response.ReservationResponse;
import com.demo.hotel_reservation_api.domain.exception.Reservation.ReservationNotFoundException;
import com.demo.hotel_reservation_api.domain.exception.auth.UnauthorizedAccessException;
import com.demo.hotel_reservation_api.domain.exception.auth.UserNotFoundException;
import com.demo.hotel_reservation_api.domain.model.Reservation;
import com.demo.hotel_reservation_api.domain.model.User;
import com.demo.hotel_reservation_api.domain.repository.ReservationRepository;
import com.demo.hotel_reservation_api.domain.repository.UserRepository;
import com.demo.hotel_reservation_api.domain.valueobject.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetReservationUseCase {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public ReservationResponse execute(Long reservationId, String userEmail) {

        Email email = new Email(userEmail);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(userEmail));

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException(reservationId));

        if (!reservation.getUser().getId().equals(user.getId()) && !user.isAdmin()) {
            throw new UnauthorizedAccessException("You don't have permission to view this reservation");
        }

        return mapToResponse(reservation);
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
}
