package com.demo.hotel_reservation_api.domain.model;

import com.demo.hotel_reservation_api.domain.model.enums.UserRole;
import com.demo.hotel_reservation_api.domain.valueobject.Email;
import lombok.Getter;

@Getter
public class User {
    private Long id;
    private String firstname;
    private String lastname;
    private Email email;
    private String password;
    private UserRole role;

    public User(String firstname, String lastname, Email email, String hashedPassword, UserRole role) {
        if (firstname == null ||  lastname== null || firstname.isBlank() || lastname.isBlank()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = hashedPassword;
        this.role = role;
    }

    public User(Long id, String firstname, String lastname, Email email, String password, UserRole role) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public boolean isAdmin() {
        return role == UserRole.ADMIN;
    }

    public boolean isClient() {
        return role == UserRole.CLIENT;
    }
}