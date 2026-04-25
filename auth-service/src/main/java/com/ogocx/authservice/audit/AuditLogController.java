package com.ogocx.authservice.audit;

import com.ogocx.servicelib.dtos.PaginationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/audit")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogUseCase auditLogUseCase;

    @GetMapping
    public ResponseEntity<PaginationDTO<AuditLogDocument>> search(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String action,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(auditLogUseCase.search(email, action, status, page, size));
    }
}