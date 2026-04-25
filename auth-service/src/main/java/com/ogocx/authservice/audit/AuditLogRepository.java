package com.ogocx.authservice.audit;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface AuditLogRepository extends ElasticsearchRepository<AuditLogDocument, String> {
}