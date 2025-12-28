package com.demo.hotel_reservation_api.infrastructure.security;

import com.demo.hotel_reservation_api.domain.exception.UserNotFoundException;
import com.demo.hotel_reservation_api.domain.model.User;
import com.demo.hotel_reservation_api.domain.repository.UserRepository;
import com.demo.hotel_reservation_api.domain.valueobject.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Bridges Spring Security UserDetailsService with domain UserRepository.
 * Loads users by email (used as username) for authentication.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String emailString) throws UsernameNotFoundException {
        Email email;
        try {
            email = new Email(emailString);
        } catch (IllegalArgumentException e) {
            throw new UsernameNotFoundException("Invalid email format: " + emailString);
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + emailString));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail().value())
                .password(user.getPassword())
                .authorities(Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
                ))
                .build();
    }
}