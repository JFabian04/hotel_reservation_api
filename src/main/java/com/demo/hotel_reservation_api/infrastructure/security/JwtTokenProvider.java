package com.demo.hotel_reservation_api.infrastructure.security;

import com.demo.hotel_reservation_api.application.port.TokenProvider;
import com.demo.hotel_reservation_api.domain.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT token provider using JJWT library.
 * Handles token generation, validation, and claim extraction.
 */

@Component
public class JwtTokenProvider implements TokenProvider {

    @Value("${jwt.secret:mySecretKeyThatIsAtLeast256BitsLongForHS256Algorithm123456}")
    private String secret;

    @Value("${jwt.expiration:86400000}")
    private Long expiration;

    @Override
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole().name());
        claims.put("email", user.getEmail().value());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail().value())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String validateTokenAndGetUsername(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    @Override
    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}