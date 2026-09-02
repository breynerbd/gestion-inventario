package com.gestioninventario.backend.infrastructure.security;

import com.gestioninventario.backend.domain.entity.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

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

    public String generateAccessToken(Usuario usuario) {

        Map<String, Object> claims = Map.of(
                "tipo", "ACCESS",
                "rol", usuario.getRol() != null
                        ? usuario.getRol().getNombre_rol()
                        : ""
        );

        return Jwts.builder()
                .claims(claims)
                .subject(usuario.getCorreo_electronico())
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + accessExpiration
                        )
                )
                .signWith(getSigningKey())
                .compact();
    }

    public String generateRefreshToken(Usuario usuario) {

        Map<String, Object> claims = Map.of(
                "tipo", "REFRESH"
        );

        return Jwts.builder()
                .claims(claims)
                .subject(usuario.getCorreo_electronico())
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + refreshExpiration
                        )
                )
                .signWith(getSigningKey())
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, claims -> claims.getSubject());
    }

    public String extractTipo(String token) {
        return extractClaim(
                token,
                claims -> claims.get("tipo", String.class)
        );
    }

    public boolean isRefreshTokenValid(
            String token,
            String correoElectronico) {

        String username = extractUsername(token);

        return username.equals(correoElectronico)
                && !isTokenExpired(token)
                && "REFRESH".equals(extractTipo(token));
    }

    public boolean isTokenValid(
            String token,
            String correoElectronico) {

        String username = extractUsername(token);

        return username.equals(correoElectronico)
                && !isTokenExpired(token)
                && "ACCESS".equals(extractTipo(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, claims -> claims.getExpiration());
    }

    private <T> T extractClaim(
            String token,
            Function<Claims, T> claimsResolver) {

        Claims claims = extractAllClaims(token);

        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}