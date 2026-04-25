package com.ogocx.authservice.messaging;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import tools.jackson.databind.ObjectMapper;
import com.ogocx.ogocxbus.routing.RoutingKeys;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigMessaging {

    @Value("${rabbitmq.queue.user.created}")
    private String userCreatedQueue;

    @Value("${rabbitmq.queue.user.updated}")
    private String userUpdatedQueue;

    @Value("${rabbitmq.exchange.user}")
    private String userExchange;

    @Bean
    public Queue userCreatedQueue() {
        return new Queue(userCreatedQueue, true);
    }

    @Bean
    public Queue userUpdatedQueue() {
        return new Queue(userUpdatedQueue, true);
    }

    @Bean
    public TopicExchange userExchange() {
        return new TopicExchange(userExchange);
    }

    @Bean
    public Binding userCreatedBinding(Queue userCreatedQueue, TopicExchange userExchange) {
        return BindingBuilder.bind(userCreatedQueue).to(userExchange).with(RoutingKeys.User.CREATED);
    }

    @Bean
    public Binding userUpdatedBinding(Queue userUpdatedQueue, TopicExchange userExchange) {
        return BindingBuilder.bind(userUpdatedQueue).to(userExchange).with(RoutingKeys.User.UPDATED);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        com.fasterxml.jackson.databind.ObjectMapper objectMapper =
                new com.fasterxml.jackson.databind.ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}