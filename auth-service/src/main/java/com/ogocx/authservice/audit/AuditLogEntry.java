package com.ogocx.authservice.audit;

import lombok.Builder;

import java.util.UUID;

@Builder
public record AuditLogEntry(
        UUID userId,
        String email,
        String action,
        String status,
        String description
) {}