package com.demo.hotel_reservation_api.domain.repository;

import com.demo.hotel_reservation_api.domain.model.User;
import com.demo.hotel_reservation_api.domain.valueobject.Email;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findByEmail(Email email);

    Optional<User> findById(Long id);

    boolean existsByEmail(String username);

    boolean existsByEmail(Email email);
}