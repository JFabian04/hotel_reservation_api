package com.demo.hotel_reservation_api.application.usecase.reservation;

import com.demo.hotel_reservation_api.application.dto.response.ReservationResponse;
import com.demo.hotel_reservation_api.domain.exception.Reservation.ReservationNotFoundException;
import com.demo.hotel_reservation_api.domain.model.Reservation;
import com.demo.hotel_reservation_api.domain.model.Room;
import com.demo.hotel_reservation_api.domain.repository.ReservationRepository;
import com.demo.hotel_reservation_api.domain.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConfirmReservationUseCase {

    private final ReservationRepository reservationRepository;

    @Transactional
    public ReservationResponse execute(Long reservationId) {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException(reservationId));

        Room room = reservation.getRoom();

        if (room == null) {
            throw new IllegalStateException("Reservation hasn't a room relation");
        }
        room.markAsOccupied();

        reservation.confirm();
        Reservation updatedReservation = reservationRepository.save(reservation);

        return mapToResponse(updatedReservation);
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
