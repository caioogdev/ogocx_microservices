package com.ogocx.authservice.tokens.mappers;

import com.ogocx.authservice.tokens.dtos.TokenClaimsDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TokenMapper {

    @Value("${spring.jwt.expiration.access}")
    private Long accessExpiration;

    @Value("${spring.jwt.expiration.refresh}")
    private Long refreshExpiration;


    public TokenClaimsDTO AccessClaimsMapper(UUID userId, String email){
        return new TokenClaimsDTO(userId, email, accessExpiration, "access");
    }

    public TokenClaimsDTO RefreshClaimsMapper(UUID userId, String email){
        return new TokenClaimsDTO(userId, email, refreshExpiration, "refresh");
    }
}
