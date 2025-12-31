package com.demo.hotel_reservation_api.infrastructure.persistence.mapper;

import com.demo.hotel_reservation_api.domain.model.Room;
import com.demo.hotel_reservation_api.infrastructure.persistence.entity.RoomEntity;
import org.springframework.stereotype.Component;

@Component
public class RoomEntityMapper {
    public RoomEntity toEntity(Room room) {
        return new RoomEntity(
                room.getId(),
                room.getNumber(),
                room.getType(),
                room.getPricePerNight(),
                room.getStatus()
        );
    }
    public Room toDomain(RoomEntity entity) {
        return new Room(
                entity.getId(),
                entity.getNumber(),
                entity.getType(),
                entity.getPricePerNight(),
                entity.getStatus()
        );
    }
}
