package com.ogocx.servicelib.web.reactive;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import com.ogocx.servicelib.context.CorrelationContext;
import com.ogocx.servicelib.error.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.webflux.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Order(-2)
@Slf4j
public class ReactiveGlobalExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;

    public ReactiveGlobalExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ApiError body = mapException(ex, exchange);
        return write(exchange, body);
    }

    private ApiError mapException(Throwable ex, ServerWebExchange exchange) {
        String cid = exchange.getRequest().getHeaders().getFirst(CorrelationContext.HEADER);
        String path = exchange.getRequest().getURI().getPath();

        if (ex instanceof DomainException de) {
            ErrorCode ec = de.errorCode();
            log.warn("Domain exception [{}]: {}", ec.code(), de.getMessage());
            return buildBody(ec, de.getMessage(), path, de.violations(), cid);
        }

        if (ex instanceof ResponseStatusException rse) {
            CommonError ec = switch (rse.getStatusCode().value()) {
                case 400 -> CommonError.MALFORMED_REQUEST;
                case 401 -> CommonError.UNAUTHORIZED;
                case 403 -> CommonError.FORBIDDEN;
                case 404 -> CommonError.RESOURCE_NOT_FOUND;
                case 405 -> CommonError.METHOD_NOT_ALLOWED;
                case 409 -> CommonError.CONFLICT;
                default  -> CommonError.INTERNAL_ERROR;
            };
            String detail = rse.getReason() != null ? rse.getReason() : ec.defaultMessage();
            return buildBody(ec, detail, path, List.of(), cid);
        }

        log.error("Unhandled exception [correlationId={}]", cid, ex);
        return buildBody(CommonError.INTERNAL_ERROR, "Unexpected error", path, List.of(), cid);
    }

    private ApiError buildBody(ErrorCode ec, String detail, String path,
                               List<FieldViolation> violations, String cid) {
        return ApiError.builder()
                .status(ec.status().value())
                .title(ec.defaultMessage())
                .detail(detail)
                .code(ec.code())
                .instance(path)
                .correlationId(cid)
                .violations(violations)
                .build();
    }

    private Mono<Void> write(ServerWebExchange exchange, ApiError body) {
        exchange.getResponse().setStatusCode(org.springframework.http.HttpStatusCode.valueOf(body.status()));
        exchange.getResponse().getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PROBLEM_JSON_VALUE);

        byte[] bytes;
        try {
            bytes = objectMapper.writeValueAsBytes(body);
        } catch (JacksonException e) {
            bytes = ("{\"status\":500,\"code\":\"INTERNAL_ERROR\",\"detail\":\"serialization failed\"}")
                    .getBytes();
        }
        DataBufferFactory factory = exchange.getResponse().bufferFactory();
        DataBuffer buffer = factory.wrap(bytes);
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
}