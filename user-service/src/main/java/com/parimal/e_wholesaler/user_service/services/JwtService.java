package com.parimal.e_wholesaler.user_service.services;

import com.parimal.e_wholesaler.user_service.entities.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value(value = "${secret.key}")
    private String secretKey;


    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(UserEntity user) {
        return Jwts.builder()
                .signWith(getSecretKey())
                .subject(user.getId().toString())
                .claim("username", user.getUsername())
                .claim("roles", user.getUserType())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + (1000L * 60 * 10)))
                .compact();
    }

    public String generateRefreshToken(UserEntity user) {
        return Jwts.builder()
                .signWith(getSecretKey())
                .subject(user.getId().toString())
                .claim("username", user.getUsername())
                .claim("roles", user.getUserType())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 180)))
                .compact();
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return Long.parseLong(claims.getSubject());
    }

}
