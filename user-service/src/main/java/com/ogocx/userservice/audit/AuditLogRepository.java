package com.ogocx.userservice.audit;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface AuditLogRepository extends ElasticsearchRepository<AuditLogDocument, String> {
}