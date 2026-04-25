package com.ogocx.authservice.tokens.dtos;

import jakarta.validation.constraints.NotBlank;

public record LogoutReqDTO(
        @NotBlank String refreshToken
) {}
