package com.ogocx.userservice.messaging;

import com.ogocx.ogocxbus.routing.RoutingKeys;
import com.ogocx.userservice.dtos.UserCreatedMessageDTO;
import com.ogocx.userservice.dtos.UserUpdatedMessageDTO;
import com.ogocx.userservice.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserEventPublisher {

    @Value("${rabbitmq.exchange.user}")
    private String userExchange;

    private final RabbitTemplate rabbitTemplate;
    private final UserMapper userMapper;

    public void publishUserCreated(UserCreatedMessageDTO dto) {
        try {
            rabbitTemplate.convertAndSend(userExchange, RoutingKeys.User.CREATED, userMapper.UserCreatedMessageMapper(dto));
        } catch (Exception e) {
            log.error("Failed to publish UserCreatedEvent for userId {}: {}", dto.id(), e.getMessage(), e);
        }
    }

    public void publishUserUpdated(UserUpdatedMessageDTO dto) {
        try {
            rabbitTemplate.convertAndSend(userExchange, RoutingKeys.User.UPDATED, userMapper.UserUpdatedMessageMapper(dto));
        } catch (Exception e) {
            log.error("Failed to publish UserUpdatedEvent for userId {}: {}", dto.id(), e.getMessage(), e);
        }
    }
}
