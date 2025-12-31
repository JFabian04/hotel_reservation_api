package com.demo.hotel_reservation_api.infrastructure.persistence.adapter;

import com.demo.hotel_reservation_api.domain.model.Reservation;
import com.demo.hotel_reservation_api.domain.model.enums.ReservationStatus;
import com.demo.hotel_reservation_api.domain.repository.ReservationRepository;
import com.demo.hotel_reservation_api.infrastructure.persistence.entity.ReservationEntity;
import com.demo.hotel_reservation_api.infrastructure.persistence.jpa.ReservationJpaRepository;
import com.demo.hotel_reservation_api.infrastructure.persistence.mapper.ReservationEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
@RequiredArgsConstructor
public class ReservationRepositoryAdapter implements ReservationRepository {

    private final ReservationJpaRepository jpaRepository;
    private final ReservationEntityMapper mapper;

    @Override
    public Reservation save(Reservation reservation) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(reservation)));
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public PageResult<Reservation> findByUserId(Long userId, ReservationStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReservationEntity> result = jpaRepository.findByUserIdAndStatus(userId, status, pageable);

        return new PageResult<>(
                result.getContent().stream().map(mapper::toDomain).toList(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages(),
                result.hasNext(),
                result.hasPrevious()
        );
    }

    @Override
    public PageResult<Reservation> findAll(Long roomId, Long userId, ReservationStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReservationEntity> result = jpaRepository.findAllWithFilters(roomId, userId, status, pageable);

        return new PageResult<>(
                result.getContent().stream().map(mapper::toDomain).toList(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages(),
                result.hasNext(),
                result.hasPrevious()
        );
    }

    @Override
    public boolean existsConflictingReservation(Long roomId, LocalDate checkIn, LocalDate checkOut, Long excludeReservationId) {
        return jpaRepository.existsConflictingReservation(roomId, checkIn, checkOut, excludeReservationId);
    }

    @Override
    public List<Reservation> findActiveReservationsByRoomAndDateRange(Long roomId, LocalDate checkIn, LocalDate checkOut) {
        return jpaRepository.findActiveReservationsByRoomAndDateRange(roomId, checkIn, checkOut)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}