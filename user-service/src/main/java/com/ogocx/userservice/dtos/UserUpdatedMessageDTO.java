package com.ogocx.userservice.dtos;

import java.util.UUID;

public record UserUpdatedMessageDTO(
        UUID id,
        String email,
        String passwordHash
) {}