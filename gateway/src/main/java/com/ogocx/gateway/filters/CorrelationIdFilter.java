package com.ogocx.gateway.filters;

import org.jspecify.annotations.NullMarked;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@NullMarked
@Component
public class CorrelationIdFilter implements GlobalFilter, Ordered {

    public static final String HEADER = "X-Correlation-Id";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String existing = exchange.getRequest().getHeaders().getFirst(HEADER);
        String correlationId = (existing != null && !existing.isBlank())
                ? existing
                : UUID.randomUUID().toString();

        ServerHttpRequest mutated = exchange.getRequest().mutate()
                .header(HEADER, correlationId)
                .build();

        exchange.getResponse().beforeCommit(() -> {
            exchange.getResponse().getHeaders().putIfAbsent(
                    HEADER, List.of(correlationId)
            );
            return Mono.empty();
        });

        return chain.filter(exchange.mutate().request(mutated).build());
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}