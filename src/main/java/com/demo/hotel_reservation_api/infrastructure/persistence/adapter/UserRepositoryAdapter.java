package com.demo.hotel_reservation_api.infrastructure.persistence.adapter;


import com.demo.hotel_reservation_api.domain.model.User;
import com.demo.hotel_reservation_api.domain.repository.UserRepository;
import com.demo.hotel_reservation_api.domain.valueobject.Email;
import com.demo.hotel_reservation_api.infrastructure.persistence.entity.UserEntity;
import com.demo.hotel_reservation_api.infrastructure.persistence.jpa.UserJpaRepository;
import com.demo.hotel_reservation_api.infrastructure.persistence.mapper.UserEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository jpaRepository;
    private final UserEntityMapper mapper;

    @Override
    public User save(User user) {
        UserEntity entity = mapper.toEntity(user);
        UserEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return jpaRepository.findByEmail(email.value())
                .map(mapper::toDomain);
    }

    @Override
    public Optional<User> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String username) {
        return jpaRepository.existsByEmail(username);
    }

    @Override
    public boolean existsByEmail(Email email) {
        return jpaRepository.existsByEmail(email.value());
    }
}
