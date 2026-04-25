package com.ogocx.servicelib.autoconfigure;

import com.ogocx.servicelib.web.servlet.CorrelationIdFilter;
import com.ogocx.servicelib.web.servlet.GlobalExceptionHandler;
import com.ogocx.servicelib.web.servlet.SecurityExceptionHandler;
import jakarta.servlet.Servlet;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(Servlet.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class WebCommonsServletAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public CorrelationIdFilter correlationIdFilter() { return new CorrelationIdFilter(); }

    @Bean
    @ConditionalOnMissingBean
    public GlobalExceptionHandler globalExceptionHandler() { return new GlobalExceptionHandler(); }

    @Bean
    @ConditionalOnMissingBean(SecurityExceptionHandler.class)
    @ConditionalOnClass(name = "org.springframework.security.access.AccessDeniedException")
    public SecurityExceptionHandler securityExceptionHandler() {
        return new SecurityExceptionHandler();
    }
}
