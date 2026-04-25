package com.ogocx.authservice.tokens.dtos;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenReqDTO(
    @NotBlank(message = "Refresh token is required")
    String refreshToken
) { }
