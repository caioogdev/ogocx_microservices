package com.ogocx.authservice.tokens.mappers;

import com.ogocx.authservice.tokens.dtos.TokenClaimsDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TokenMapper {

    private final long accessExpiration;
    private final long refreshExpiration;

    public TokenMapper(
            @Value("${spring.jwt.expiration.access}") long accessExpiration,
            @Value("${spring.jwt.expiration.refresh}") long refreshExpiration
    ) {
        this.accessExpiration = accessExpiration;
        this.refreshExpiration = refreshExpiration;
    }

    public TokenClaimsDTO AccessClaims(UUID userId, String email) {
        return new TokenClaimsDTO(userId, email, accessExpiration, "access");
    }

    public TokenClaimsDTO RefreshClaims(UUID userId, String email) {
        return new TokenClaimsDTO(userId, email, refreshExpiration, "refresh");
    }
}