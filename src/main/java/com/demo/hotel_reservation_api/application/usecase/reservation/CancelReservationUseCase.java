package com.demo.hotel_reservation_api.application.usecase.reservation;

import com.demo.hotel_reservation_api.domain.exception.Reservation.InvalidStateTransitionException;
import com.demo.hotel_reservation_api.domain.exception.Reservation.ReservationNotFoundException;
import com.demo.hotel_reservation_api.domain.exception.auth.UnauthorizedAccessException;
import com.demo.hotel_reservation_api.domain.exception.auth.UserNotFoundException;
import com.demo.hotel_reservation_api.domain.model.Reservation;
import com.demo.hotel_reservation_api.domain.model.User;
import com.demo.hotel_reservation_api.domain.repository.ReservationRepository;
import com.demo.hotel_reservation_api.domain.repository.UserRepository;
import com.demo.hotel_reservation_api.domain.valueobject.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CancelReservationUseCase {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    @Transactional
    public void execute(Long reservationId, String userEmail) {

        log.info("Attempting to cancel reservation: {} by user: {}", reservationId, userEmail);

        Email email = new Email(userEmail);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(userEmail));

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException(reservationId));

        boolean isOwner = reservation.getUser().getId().equals(user.getId());
        boolean isAdmin = user.isAdmin();

        if (!isOwner && !isAdmin) {
            throw new UnauthorizedAccessException("You don't have permission to cancel this reservation");
        }

        if (!isAdmin && !reservation.canBeCancelledByUser()) {
            throw new InvalidStateTransitionException(
                    "Cannot cancel this reservation. It may be too close to check-in date or already completed/cancelled"
            );
        }

        reservation.cancel();

        reservationRepository.save(reservation);

        log.info("Reservation {} cancelled successfully", reservationId);
    }
}
