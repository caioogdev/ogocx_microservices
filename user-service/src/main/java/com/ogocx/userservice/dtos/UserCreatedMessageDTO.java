package com.ogocx.userservice.dtos;

import java.util.UUID;

public record UserCreatedMessageDTO(
        UUID id,
        String email,
        String passwordHash
) {}