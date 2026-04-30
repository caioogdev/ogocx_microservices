package com.ogocx.authservice.tokens;

import com.ogocx.authservice.tokens.dtos.TokenClaimsDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtUseCase {

    private final SecretKey secretKey;

    public JwtUseCase(@Value("${spring.jwt.secret}") String secret) {
        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException("JWT secret is not configured");
        }
        byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);
        if (bytes.length < 32) {
            throw new IllegalStateException(
                    "JWT secret must be at least 256 bits (32 chars). Got " + bytes.length * 8 + " bits.");
        }
        this.secretKey = Keys.hmacShaKeyFor(bytes);
    }

    public String generate(TokenClaimsDTO dto) {
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(dto.userId().toString())
                .claim("email", dto.email())
                .claim("type", dto.type())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusSeconds(dto.expiration())))
                .signWith(secretKey)
                .compact();
    }

    public boolean isValid(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isRefreshToken(String token) {
        return "refresh".equals(extractClaim(token, "type"));
    }

    public UUID extractUserId(String token) {
        return UUID.fromString(getClaims(token).getSubject());
    }

    public String extractEmail(String token) {
        return extractClaim(token, "email");
    }

    private String extractClaim(String token, String claim) {
        return getClaims(token).get(claim, String.class);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}