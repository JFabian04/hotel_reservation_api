package com.demo.hotel_reservation_api.domain.repository;

import com.demo.hotel_reservation_api.domain.model.Room;
import com.demo.hotel_reservation_api.domain.model.enums.RoomStatus;
import com.demo.hotel_reservation_api.domain.model.enums.RoomType;

import java.util.List;
import java.util.Optional;

public interface RoomRepository {
    Room save(Room room);
    Optional<Room> findById(Long id);
    Optional<Room> findByNumber(String number);
    PageResult<Room> findAll(int page, int size);
    PageResult<Room> findByFilters(RoomType type, RoomStatus status, int page, int size);
    boolean existsByNumber(String number);
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
