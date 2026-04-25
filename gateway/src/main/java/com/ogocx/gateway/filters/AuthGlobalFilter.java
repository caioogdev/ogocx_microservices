package com.ogocx.gateway.filters;

import com.ogocx.gateway.config.AppGatewayProperties;
import com.ogocx.gateway.usecases.JwtUseCase;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@NullMarked
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private final JwtUseCase jwtUseCase;
    private final AppGatewayProperties gatewayProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        if (isPublic(path)) {
            return chain.filter(exchange);
        }

        String authHeader = request.getHeaders().getFirst("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return reject(exchange.getResponse(), "Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);

        if (!jwtUseCase.isValid(token) || jwtUseCase.isRefreshToken(token)) {
            return reject(exchange.getResponse(), "Invalid or expired token");
        }

        ServerHttpRequest mutatedRequest = request.mutate()
                .header("X-User-Id", jwtUseCase.extractUserId(token).toString())
                .header("X-User-Email", jwtUseCase.extractEmail(token))
                .build();

        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }

    private boolean isPublic(String path) {
        return gatewayProperties.getPublicRoutes().stream().anyMatch(path::startsWith);
    }

    private Mono<Void> reject(ServerHttpResponse response, String detail) {
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_PROBLEM_JSON);
        String body = """
                {"status":401,"code":"UNAUTHORIZED","title":"Authentication required","detail":"%s"}
                """.formatted(detail).strip();
        var buffer = response.bufferFactory().wrap(body.getBytes());
        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }
}