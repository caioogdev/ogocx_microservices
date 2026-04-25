package com.ogocx.servicelib.web.reactive;

import com.ogocx.servicelib.context.CorrelationContext;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

public final class ReactiveCorrelationIdFilter implements WebFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String cid = CorrelationContext.resolve(
                exchange.getRequest().getHeaders().getFirst(CorrelationContext.HEADER));

        ServerHttpRequest mutated = exchange.getRequest().mutate()
                .header(CorrelationContext.HEADER, cid).build();
        exchange.getResponse().getHeaders().set(CorrelationContext.HEADER, cid);

        return chain.filter(exchange.mutate().request(mutated).build())
                .contextWrite(ctx -> ctx.put(CorrelationContext.REACTOR_KEY, cid));
    }

    @Override public int getOrder() { return Ordered.HIGHEST_PRECEDENCE; }
}
