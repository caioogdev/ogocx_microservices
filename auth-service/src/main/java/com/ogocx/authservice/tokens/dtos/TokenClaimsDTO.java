package com.ogocx.authservice.tokens.dtos;

import java.util.UUID;

public record TokenClaimsDTO (
        UUID userId,
        String email,
        Long expiration,
        String type
){ }
