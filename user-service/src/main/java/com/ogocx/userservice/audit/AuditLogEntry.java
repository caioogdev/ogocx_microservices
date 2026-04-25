package com.ogocx.userservice.audit;

import lombok.Builder;
import java.util.UUID;

@Builder
public record AuditLogEntry (
    UUID userId,
    UUID actorId,
    String action,
    String status,
    String description
){ }
