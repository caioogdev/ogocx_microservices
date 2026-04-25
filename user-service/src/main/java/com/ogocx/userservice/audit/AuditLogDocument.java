package com.ogocx.userservice.audit;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.Instant;
import java.util.UUID;

@Document(indexName = "audit_user_logs")
@Getter
@Setter
@NoArgsConstructor
@JsonPropertyOrder({"id","userId", "actorId", "action", "status", "description", "occurredAt"})
public class AuditLogDocument {

    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private UUID userId;

    @Field(type = FieldType.Keyword)
    private UUID actorId;

    @Field(type = FieldType.Keyword)
    private String action;

    @Field(type = FieldType.Keyword)
    private String status;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Date, format = {DateFormat.date_optional_time, DateFormat.epoch_millis})
    private Instant occurredAt;
}
