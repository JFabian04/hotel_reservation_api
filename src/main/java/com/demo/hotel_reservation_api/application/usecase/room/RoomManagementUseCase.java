package com.demo.hotel_reservation_api.application.usecase.room;

import com.demo.hotel_reservation_api.application.dto.request.CreateRoomRequest;
import com.demo.hotel_reservation_api.application.dto.request.UpdateRoomRequest;
import com.demo.hotel_reservation_api.application.dto.response.PagedRoomResponse;
import com.demo.hotel_reservation_api.application.dto.response.RoomResponse;
import com.demo.hotel_reservation_api.domain.exception.room.RoomAlreadyExistsException;
import com.demo.hotel_reservation_api.domain.exception.room.RoomNotFoundException;
import com.demo.hotel_reservation_api.domain.model.Room;
import com.demo.hotel_reservation_api.domain.model.enums.RoomStatus;
import com.demo.hotel_reservation_api.domain.model.enums.RoomType;
import com.demo.hotel_reservation_api.domain.repository.RoomRepository;
import com.demo.hotel_reservation_api.domain.repository.RoomRepository.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomManagementUseCase {
    private final RoomRepository roomRepository;

    @Transactional(readOnly = true)
    public PagedRoomResponse execute(RoomType type, RoomStatus status, int page, int size) {

        validatePaginationParams(page, size);

        PageResult<Room> pageResult;
        if (type != null || status != null) {
            pageResult = roomRepository.findByFilters(type, status, page, size);
        } else {
            pageResult = roomRepository.findAll(page, size);
        }

        List<RoomResponse> roomResponses = pageResult.content().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return new PagedRoomResponse(
                roomResponses,
                pageResult.pageNumber(),
                pageResult.pageSize(),
                pageResult.totalElements(),
                pageResult.totalPages(),
                pageResult.hasNext(),
                pageResult.hasPrevious()
        );
    }

    @Transactional(readOnly = true)
    public RoomResponse getById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RoomNotFoundException(id));

        return mapToResponse(room);
    }

    @Transactional
    public RoomResponse create(CreateRoomRequest request) {
        if (roomRepository.existsByNumber(request.number())) {
            throw new RoomAlreadyExistsException(request.number());
        }
        Room room = new Room(
                request.number(),
                request.type(),
                request.pricePerNight()
        );
        Room savedRoom = roomRepository.save(room);

        return mapToResponse(savedRoom);
    }

    @Transactional
    public RoomResponse update(Long id, UpdateRoomRequest request) {
        Room existingRoom = roomRepository.findById(id)
                .orElseThrow(() -> new RoomNotFoundException(id));

        Room updatedRoom = new Room(
                existingRoom.getId(),
                existingRoom.getNumber(),
                request.type() != null ? request.type() : existingRoom.getType(),
                request.pricePerNight() != null ? request.pricePerNight() : existingRoom.getPricePerNight(),
                request.status() != null ? request.status() : existingRoom.getStatus()
        );

        Room savedRoom = roomRepository.save(updatedRoom);

        return mapToResponse(savedRoom);
    }

    @Transactional
    public void delete(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RoomNotFoundException(id));

        roomRepository.save(room);
    }

    @Transactional
    public RoomResponse changeStatus(Long id, RoomStatus newStatus) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RoomNotFoundException(id));

        Room updatedRoom = new Room(
                room.getId(),
                room.getNumber(),
                room.getType(),
                room.getPricePerNight(),
                newStatus
        );

        Room savedRoom = roomRepository.save(updatedRoom);

        return mapToResponse(savedRoom);
    }

    // ========== PRIVATE HELPERS ==========
    private RoomResponse mapToResponse(Room room) {
        return new RoomResponse(
                room.getId(),
                room.getNumber(),
                room.getType().name(),
                room.getPricePerNight(),
                room.getStatus().name(),
                room.isAvailable()
        );
    }

    private void validatePaginationParams(int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("Page number cannot be negative");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Page size must be greater than 0");
        }
        if (size > 100) {
            throw new IllegalArgumentException("Page size cannot exceed 100");
        }
    }
}
