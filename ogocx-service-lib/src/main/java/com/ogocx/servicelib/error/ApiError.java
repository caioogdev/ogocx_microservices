package com.ogocx.servicelib.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.annotation.Nullable;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiError(
        URI type,
        String title,
        int status,
        String detail,
        String instance,
        String code,
        String correlationId,
        Instant timestamp,
        List<FieldViolation> violations
) {
    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private URI type;
        private String title;
        private int status;
        private String detail;
        private String instance;
        private String code;
        private String correlationId;
        private Instant timestamp = Instant.now();
        private List<FieldViolation> violations = List.of();

        public Builder type(URI v)                          { this.type = v; return this; }
        public Builder title(String v)                      { this.title = v; return this; }
        public Builder status(int v)                        { this.status = v; return this; }
        public Builder detail(String v)                     { this.detail = v; return this; }
        public Builder instance(String v)                   { this.instance = v; return this; }
        public Builder code(@Nullable String v)             { this.code = Objects.requireNonNull(v); return this; }
        public Builder correlationId(@Nullable String v)    { this.correlationId = v; return this; }
        public Builder timestamp(Instant v)                 { this.timestamp = v; return this; }
        public Builder violations(List<FieldViolation> v) {
            this.violations = v == null ? List.of() : List.copyOf(v); return this;
        }

        public ApiError build() {
            if (status < 100 || status > 599)
                throw new IllegalStateException("status must be a valid HTTP code");
            if (code == null || code.isBlank())
                throw new IllegalStateException("code is required");
            return new ApiError(type, title, status, detail, instance, code,
                    correlationId, timestamp, violations);
        }
    }
}