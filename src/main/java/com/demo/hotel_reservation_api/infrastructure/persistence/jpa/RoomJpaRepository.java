package com.demo.hotel_reservation_api.infrastructure.persistence.jpa;

import com.demo.hotel_reservation_api.domain.model.enums.RoomStatus;
import com.demo.hotel_reservation_api.domain.model.enums.RoomType;
import com.demo.hotel_reservation_api.infrastructure.persistence.entity.RoomEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoomJpaRepository  extends JpaRepository<RoomEntity, Long> {
    Optional<RoomEntity> findByNumberAndIsActiveTrue(String number);

    boolean existsByNumberAndIsActiveTrue(String number);

    Page<RoomEntity> findByIsActiveTrue(Pageable pageable);

    @Query("SELECT r FROM RoomEntity r WHERE r.isActive = true " +
            "AND (:type IS NULL OR r.type = :type) " +
            "AND (:status IS NULL OR r.status = :status)")
    Page<RoomEntity> findByFilters(
            @Param("type") RoomType type,
            @Param("status") RoomStatus status,
            Pageable pageable
    );
}
