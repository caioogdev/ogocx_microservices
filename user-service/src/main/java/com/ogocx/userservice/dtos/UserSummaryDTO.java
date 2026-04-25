package com.ogocx.userservice.dtos;

import java.util.UUID;

public record UserSummaryDTO(
        UUID id,
        String firstName,
        String lastName,
        String email
) { }
