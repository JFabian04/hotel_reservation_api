package com.demo.hotel_reservation_api.application.usecase.reservation;

import com.demo.hotel_reservation_api.application.dto.request.CreateReservationRequest;
import com.demo.hotel_reservation_api.application.dto.response.ReservationResponse;
import com.demo.hotel_reservation_api.domain.exception.room.RoomNotAvailableException;
import com.demo.hotel_reservation_api.domain.exception.room.RoomNotFoundException;
import com.demo.hotel_reservation_api.domain.exception.auth.UserNotFoundException;
import com.demo.hotel_reservation_api.domain.model.Reservation;
import com.demo.hotel_reservation_api.domain.model.Room;
import com.demo.hotel_reservation_api.domain.model.User;
import com.demo.hotel_reservation_api.domain.repository.ReservationRepository;
import com.demo.hotel_reservation_api.domain.repository.RoomRepository;
import com.demo.hotel_reservation_api.domain.repository.UserRepository;
import com.demo.hotel_reservation_api.domain.valueobject.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateReservationUseCase {
    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    @Transactional
    public ReservationResponse execute(CreateReservationRequest request, String userEmail) {
        Email email = new Email(userEmail);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(userEmail));

        Room room = roomRepository.findById(request.roomId())
                .orElseThrow(() -> new RoomNotFoundException(request.roomId()));

        validateRoomAvailability(room, request.checkIn(), request.checkOut());

        Reservation reservation = new Reservation(
                room,
                user,
                request.checkIn(),
                request.checkOut(),
                request.guestCount()
        );
        Reservation savedReservation = reservationRepository.save(reservation);

        log.info("Reservation created successfully with id: {}", savedReservation.getId());

        return mapToResponse(savedReservation);
    }

    private void validateRoomAvailability(Room room, LocalDate checkIn, LocalDate checkOut) {
        boolean hasConflict = reservationRepository.existsConflictingReservation(
                room.getId(),
                checkIn,
                checkOut,
                null
        );

        if (hasConflict) {
            throw new RoomNotAvailableException(
                    room.getNumber(),
                    checkIn + " to " + checkOut
            );
        }
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
