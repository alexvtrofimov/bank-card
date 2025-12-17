package com.example.bankcards.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@Slf4j
public class JwtUtil {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private String jwtExpirationMs;

    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String username) {
        Date date = new Date();
        return Jwts
                .builder()
                .subject(username)
                .issuedAt(date)
                .expiration(new Date(date.getTime() + Long.valueOf(jwtExpirationMs)))
                .signWith(this.key)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return Jwts
                .parser()
                .verifyWith(this.key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts
                    .parser()
                    .verifyWith(this.key)
                    .build()
                    .parseSignedClaims(token);

            return true;
        } catch (JwtException e) {
            log.error("JWT Validation error {}", e.getMessage());
        }
        return false;
    }
}
