package com.ogocx.authservice.messaging.consumers;

import com.ogocx.authservice.dtos.UserCreatedMessageDTO;
import com.ogocx.authservice.usecases.CreateCredentialUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserCreatedConsumer {

    private final CreateCredentialUseCase createCredentialUseCase;

    @RabbitListener(queues = "${rabbitmq.queue.user.created}")
    public void consume(UserCreatedMessageDTO event) {
        log.info("Received UserCreatedEvent for email: {}", event.email());
        createCredentialUseCase.execute(event);
    }
}
