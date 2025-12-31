package com.demo.hotel_reservation_api.infrastructure.persistence.mapper;

import com.demo.hotel_reservation_api.domain.model.Reservation;
import com.demo.hotel_reservation_api.infrastructure.persistence.entity.ReservationEntity;
import org.springframework.stereotype.Component;

@Component
public class ReservationEntityMapper {

    private final RoomEntityMapper roomMapper;
    private final UserEntityMapper userMapper;

    public ReservationEntityMapper(RoomEntityMapper roomMapper, UserEntityMapper userMapper) {
        this.roomMapper = roomMapper;
        this.userMapper = userMapper;
    }

    public ReservationEntity toEntity(Reservation reservation) {
        if (reservation == null) return null;

        return new ReservationEntity(
                reservation.getId(),
                roomMapper.toEntity(reservation.getRoom()),
                userMapper.toEntity(reservation.getUser()),
                reservation.getCheckInDate(),
                reservation.getCheckOutDate(),
                reservation.getTotalAmount(),
                reservation.getStatus(),
                reservation.getGuestCount()
        );
    }

    public Reservation toDomain(ReservationEntity entity) {
        if (entity == null) return null;

        return new Reservation(
                entity.getId(),
                roomMapper.toDomain(entity.getRoom()),
                userMapper.toDomain(entity.getUser()),
                entity.getCheckInDate(),
                entity.getCheckOutDate(),
                entity.getGuests(),
                entity.getTotalAmount(),
                entity.getStatus()
        );
    }
}