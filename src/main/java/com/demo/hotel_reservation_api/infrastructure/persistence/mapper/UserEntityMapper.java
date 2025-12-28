package com.demo.hotel_reservation_api.infrastructure.persistence.mapper;

import com.demo.hotel_reservation_api.domain.model.User;
import com.demo.hotel_reservation_api.domain.valueobject.Email;
import com.demo.hotel_reservation_api.infrastructure.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

/**
 * Maps between User domain model and UserEntity JPA entity.
 * Translates value objects to/from persistence format.
 */

@Component
public class UserEntityMapper {

    public UserEntity toEntity(User user) {
        return new UserEntity(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail().value(),  // Extraer string del Value Object
                user.getPassword(),
                user.getRole()
        );
    }

    public User toDomain(UserEntity entity) {
        return new User(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                new Email(entity.getEmail()),  // Crear Value Object
                entity.getPassword(),
                entity.getRole()
        );
    }
}