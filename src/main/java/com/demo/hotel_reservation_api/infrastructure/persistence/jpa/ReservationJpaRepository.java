package com.demo.hotel_reservation_api.infrastructure.persistence.jpa;

import com.demo.hotel_reservation_api.infrastructure.persistence.entity.ReservationEntity;
import com.demo.hotel_reservation_api.domain.model.enums.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationJpaRepository extends JpaRepository<ReservationEntity, Long> {

    Page<ReservationEntity> findByUserIdAndStatus(Long userId, ReservationStatus status, Pageable pageable);

    @Query("SELECT r FROM ReservationEntity r WHERE " +
            "(:roomId IS NULL OR r.room.id = :roomId) AND " +
            "(:userId IS NULL OR r.user.id = :userId) AND " +
            "(:status IS NULL OR r.status = :status)" +
            "ORDER BY r.id DESC")
    Page<ReservationEntity> findAllWithFilters(@Param("roomId") Long roomId,
                                               @Param("userId") Long userId,
                                               @Param("status") ReservationStatus status,
                                               Pageable pageable);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " +
            "FROM ReservationEntity r " +
            "WHERE r.room.id = :roomId " +
            "AND r.status in ('CONFIRMED', 'PENDING') " +
            "AND (:excludeReservationId IS NULL OR r.id != :excludeReservationId) " +
            "AND r.checkInDate < :checkOut " +
            "AND r.checkOutDate > :checkIn")
    boolean existsConflictingReservation(@Param("roomId") Long roomId,
                                         @Param("checkIn") LocalDate checkInDate,
                                         @Param("checkOut") LocalDate checkOut,
                                         @Param("excludeReservationId") Long excludeReservationId);

    @Query("SELECT r FROM ReservationEntity r WHERE " +
            "r.room.id = :roomId " +
            "AND r.status in ('CONFIRMED', 'PENDING') " +
            "AND r.checkInDate  < :checkOut " +
            "AND r.checkOutDate > :checkIn")
    List<ReservationEntity> findActiveReservationsByRoomAndDateRange(@Param("roomId") Long roomId,
                                                                     @Param("checkIn") LocalDate checkIn,
                                                                     @Param("checkOut") LocalDate checkOut);
}