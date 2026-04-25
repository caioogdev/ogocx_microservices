package com.ogocx.authservice.tokens.dtos;

public record TokenResponseDTO (
        String accessToken,
        String refreshToken
){ }
