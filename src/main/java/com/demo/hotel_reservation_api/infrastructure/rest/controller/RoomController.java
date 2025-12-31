package com.demo.hotel_reservation_api.infrastructure.rest.controller;

import com.demo.hotel_reservation_api.application.dto.request.CreateRoomRequest;
import com.demo.hotel_reservation_api.application.dto.request.UpdateRoomRequest;
import com.demo.hotel_reservation_api.application.dto.response.PagedRoomResponse;
import com.demo.hotel_reservation_api.application.dto.response.RoomResponse;
import com.demo.hotel_reservation_api.application.usecase.room.RoomManagementUseCase;
import com.demo.hotel_reservation_api.domain.model.enums.RoomStatus;
import com.demo.hotel_reservation_api.domain.model.enums.RoomType;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {
    private final RoomManagementUseCase roomManagementUseCase;

    @GetMapping
    public ResponseEntity<PagedRoomResponse> getAllRooms(
            @RequestParam(required = false) RoomType type,
            @RequestParam(required = false) RoomStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PagedRoomResponse response = roomManagementUseCase.execute(type, status, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Tag(name = "GET BY ID", description = "Get Room detail by ID")
    public ResponseEntity<RoomResponse> getRoomById(@PathVariable Long id) {
        RoomResponse response = roomManagementUseCase.getById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Tag(name = "Register Room")
    public ResponseEntity<RoomResponse> createRoom(@Valid @RequestBody CreateRoomRequest request) {
        RoomResponse response = roomManagementUseCase.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Tag(name = "Update Room by ID")
    public ResponseEntity<RoomResponse> updateRoom(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRoomRequest request
    ) {
        RoomResponse response = roomManagementUseCase.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Tag(name = "Detele Room by ID")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        roomManagementUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoomResponse> changeRoomStatus(
            @PathVariable Long id,
            @RequestBody ChangeStatusRequest request
    ) {
        RoomResponse response = roomManagementUseCase.changeStatus(id, request.status());
        return ResponseEntity.ok(response);
    }

    private record ChangeStatusRequest(RoomStatus status) {}
}
