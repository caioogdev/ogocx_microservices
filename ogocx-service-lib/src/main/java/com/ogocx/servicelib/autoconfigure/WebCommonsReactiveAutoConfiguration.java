package com.ogocx.servicelib.autoconfigure;

import com.ogocx.servicelib.web.reactive.ReactiveCorrelationIdFilter;
import com.ogocx.servicelib.web.reactive.ReactiveGlobalExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import tools.jackson.databind.ObjectMapper;

@Configuration
@ConditionalOnClass(WebFluxConfigurer.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class WebCommonsReactiveAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ReactiveCorrelationIdFilter reactiveCorrelationIdFilter() {
        return new ReactiveCorrelationIdFilter();
    }

    @Bean
    @ConditionalOnMissingBean
    public ReactiveGlobalExceptionHandler reactiveGlobalExceptionHandler(ObjectMapper objectMapper) {
        return new ReactiveGlobalExceptionHandler(objectMapper);
    }
}
