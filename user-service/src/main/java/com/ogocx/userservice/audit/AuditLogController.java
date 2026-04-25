package com.ogocx.userservice.audit;

import com.ogocx.servicelib.dtos.PaginationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/users/audit")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogUseCase auditLogUseCase;

    @GetMapping
    public ResponseEntity<PaginationDTO<AuditLogDocument>> search(
            @RequestParam(required = false) UUID userId,
            @RequestParam(required = false) UUID actorId,
            @RequestParam(required = false) String action,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(auditLogUseCase.search(userId, actorId, action, status, page, size));
    }
}