package com.ogocx.servicelib.web.servlet;

import com.ogocx.servicelib.context.CorrelationContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Order(Ordered.HIGHEST_PRECEDENCE)
public final class CorrelationIdFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String correlationId = CorrelationContext.resolve(
                request.getHeader(CorrelationContext.HEADER));

        CorrelationContext.put(correlationId);
        response.setHeader(CorrelationContext.HEADER, correlationId);
        try {
            chain.doFilter(request, response);
        } finally {
            CorrelationContext.clear();
        }
    }
}
