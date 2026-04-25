package com.ogocx.authservice.audit;

import com.ogocx.servicelib.dtos.PaginationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogUseCase {

    private final AuditLogRepository auditLogRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    @Async
    public void log(AuditLogEntry entry) {
        AuditLogDocument log = new AuditLogDocument();
        log.setUserId(entry.userId());
        log.setEmail(entry.email());
        log.setAction(entry.action());
        log.setStatus(entry.status());
        log.setDescription(entry.description());
        log.setOccurredAt(Instant.now());
        auditLogRepository.save(log);
    }

    public PaginationDTO<AuditLogDocument> search(String email, String action, String status, int page, int size) {
        Criteria criteria = new Criteria();
        if (email != null)  criteria = criteria.and("email").is(email);
        if (action != null) criteria = criteria.and("action").is(action);
        if (status != null) criteria = criteria.and("status").is(status);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "occurredAt"));
        CriteriaQuery query = new CriteriaQuery(criteria).setPageable(pageable);

        SearchHits<AuditLogDocument> hits = elasticsearchOperations.search(query, AuditLogDocument.class);
        List<AuditLogDocument> content = hits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .toList();

        long total = hits.getTotalHits();

        return new PaginationDTO<>(
                total,
                content,
                page,
                size,
                (long) (page + 1) * size < total,
                page > 0
        );
    }
}