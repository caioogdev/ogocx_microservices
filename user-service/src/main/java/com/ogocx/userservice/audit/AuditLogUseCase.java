package com.ogocx.userservice.audit;

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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuditLogUseCase {

    private final AuditLogRepository auditLogRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    @Async
    public void log(AuditLogEntry entry) {
        AuditLogDocument log = new AuditLogDocument();
        log.setUserId(entry.userId());
        log.setActorId(entry.actorId());
        log.setAction(entry.action());
        log.setStatus(entry.status());
        log.setDescription(entry.description());
        log.setOccurredAt(Instant.now());
        auditLogRepository.save(log);
    }

    public PaginationDTO<AuditLogDocument> search(UUID userId, UUID actorId, String action, String status, int page, int size) {
        Criteria criteria = new Criteria();
        if (userId != null)  criteria = criteria.and("userId").is(userId);
        if (actorId != null) criteria = criteria.and("actorId").is(actorId);
        if (action != null)  criteria = criteria.and("action").is(action);
        if (status != null)  criteria = criteria.and("status").is(status);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "occurredAt"));
        CriteriaQuery query = new CriteriaQuery(criteria).setPageable(pageable);

        SearchHits<AuditLogDocument> hits = elasticsearchOperations.search(query, AuditLogDocument.class);
        List<AuditLogDocument> content = hits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .toList();

        long total = hits.getTotalHits();
        boolean hasNext = (long) (page + 1) * size < total;
        boolean hasPrev = page > 0;

        return new PaginationDTO<>(total, content, page, size, hasNext, hasPrev);
    }
}