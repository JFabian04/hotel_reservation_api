package com.demo.hotel_reservation_api.infrastructure.persistence.adapter;

import com.demo.hotel_reservation_api.domain.model.Room;
import com.demo.hotel_reservation_api.domain.model.enums.RoomStatus;
import com.demo.hotel_reservation_api.domain.model.enums.RoomType;
import com.demo.hotel_reservation_api.domain.repository.RoomRepository;
import com.demo.hotel_reservation_api.infrastructure.persistence.entity.RoomEntity;
import com.demo.hotel_reservation_api.infrastructure.persistence.jpa.RoomJpaRepository;
import com.demo.hotel_reservation_api.infrastructure.persistence.mapper.RoomEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RoomRepositoryAdapter implements RoomRepository {
    private final RoomJpaRepository jpaRepository;
    private final RoomEntityMapper mapper;

    @Override
    public Room save(Room room) {
        RoomEntity entity = mapper.toEntity(room);
        RoomEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Room> findById(Long id) {
        return jpaRepository.findById(id)
                .filter(entity -> entity.getIsActive())
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Room> findByNumber(String number) {
        return jpaRepository.findByNumberAndIsActiveTrue(number)
                .map(mapper::toDomain);
    }

    @Override
    public PageResult<Room> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("number").ascending());

        Page<RoomEntity> pageResult = jpaRepository.findByIsActiveTrue(pageable);

        return mapToPageResult(pageResult);
    }

    @Override
    public PageResult<Room> findByFilters(RoomType type, RoomStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("number").ascending());

        Page<RoomEntity> pageResult = jpaRepository.findByFilters(type, status, pageable);

        return mapToPageResult(pageResult);
    }

    @Override
    public boolean existsByNumber(String number) {
        return jpaRepository.existsByNumberAndIsActiveTrue(number);
    }

    private PageResult<Room> mapToPageResult(Page<RoomEntity> page) {
        List<Room> rooms = page.getContent().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());

        return new PageResult<>(
                rooms,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext(),
                page.hasPrevious()
        );
    }
}
