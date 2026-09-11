package com.gestioninventario.backend.infrastructure.security;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gestioninventario.backend.domain.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

        private final String secretKey;
        private final long accessExpiration;
        private final long refreshExpiration;

        public JwtService(
                @Value("${app.security.jwt.secret}") String secretKey,
                @Value("${app.security.jwt.access-expiration}") long accessExpiration,
                @Value("${app.security.jwt.refresh-expiration}") long refreshExpiration) {

                this.secretKey = secretKey;
                this.accessExpiration = accessExpiration;
                this.refreshExpiration = refreshExpiration;
        }

        private SecretKey getSigningKey() {
                return Keys.hmacShaKeyFor(
                        secretKey.getBytes(StandardCharsets.UTF_8)
                );
        }

        public String generateAccessToken(User user) {
                Map<String, Object> claims = Map.of(
                        "type", "ACCESS",
                        "role", user.getRole() != null
                                ? user.getRole().getRoleName()
                                : ""
                );

                Instant now = Instant.now();
                Instant expiration = now.plusMillis(accessExpiration);

                return Jwts.builder()
                        .claims(claims)
                        .subject(user.getEmail())
                        .issuedAt(Date.from(now))
                        .expiration(Date.from(expiration))
                        .signWith(getSigningKey())
                        .compact();
        }

        public String generateRefreshToken(User user) {
                Map<String, Object> claims = Map.of(
                        "type", "REFRESH"
                );

                Instant now = Instant.now();
                Instant expiration = now.plusMillis(refreshExpiration);

                return Jwts.builder()
                        .claims(claims)
                        .subject(user.getEmail())
                        .issuedAt(Date.from(now))
                        .expiration(Date.from(expiration))
                        .signWith(getSigningKey())
                        .compact();
        }

        public String extractUsername(String token) {
                return extractAllClaims(token).getSubject();
        }

        public String extractType(String token) {
                return extractAllClaims(token)
                        .get("type", String.class);
        }

        public boolean isRefreshTokenValid(
                String token,
                String email) {

                String username = extractUsername(token);

                return username != null
                        && username.equals(email)
                        && !isTokenExpired(token)
                        && "REFRESH".equals(extractType(token));
        }

        public boolean isTokenValid(
                String token,
                String email) {

                String username = extractUsername(token);

                return username != null
                        && username.equals(email)
                        && !isTokenExpired(token)
                        && "ACCESS".equals(extractType(token));
        }

        private boolean isTokenExpired(String token) {
                Date expiration = extractAllClaims(token).getExpiration();

                return expiration != null
                        && expiration.toInstant().isBefore(Instant.now());
        }

        private Claims extractAllClaims(String token) {
                return Jwts.parser()
                        .verifyWith(getSigningKey())
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();
        }
}