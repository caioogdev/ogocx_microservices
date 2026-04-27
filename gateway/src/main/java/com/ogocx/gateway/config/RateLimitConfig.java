package com.ogocx.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Optional;

@Configuration
public class RateLimitConfig {

    @Bean
    public KeyResolver ipKeyResolver() {
        return exchange -> {
            String forwarded = exchange.getRequest().getHeaders().getFirst("X-Forwarded-For");
          if(forwarded != null && !forwarded.isBlank()){
              return Mono.just(forwarded.split(",")[0].trim());
          }

          return Optional.ofNullable(exchange.getRequest().getRemoteAddress())
                  .map(addr -> Mono.just(addr.getAddress().getHostAddress()))
                  .orElse(Mono.just("unknown"));
        };
    }
}
