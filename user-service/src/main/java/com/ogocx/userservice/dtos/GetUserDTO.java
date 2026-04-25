package com.ogocx.userservice.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record GetUserDTO(
        UUID id,
        String firstName,
        String lastName,
        String firstDocument,
        String secondDocument,
        LocalDate birthDate,
        String email,
        Boolean status,
        LocalDateTime createdAt,
        UserSummaryDTO createdBy,
        LocalDateTime updatedAt,
        UserSummaryDTO updatedBy
) {}
