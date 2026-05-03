package com.ogocx.gateway.controllers;

import com.ogocx.servicelib.error.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @RequestMapping("/auth")
    public Mono<ResponseEntity<ApiError>> authFallback() {
        return buildResponse("AUTH_SERVICE_UNAVAILABLE",
                "Authentication service is temporarily unavailable. Please try again in a few moments.");
    }

    @RequestMapping("/users")
    public Mono<ResponseEntity<ApiError>> usersFallback() {
        return buildResponse("USER_SERVICE_UNAVAILABLE",
                "User service is temporarily unavailable. Please try again in a few moments.");
    }

    private Mono<ResponseEntity<ApiError>> buildResponse(String code, String detail) {
        ApiError body = ApiError.builder()
                .status(503)
                .code(code)
                .detail(detail)
                .build();
        return Mono.just(ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(body));
    }
}
