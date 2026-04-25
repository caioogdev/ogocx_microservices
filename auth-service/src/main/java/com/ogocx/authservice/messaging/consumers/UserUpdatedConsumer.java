package com.ogocx.authservice.messaging.consumers;

import com.ogocx.authservice.dtos.UserUpdatedMessageDTO;
import com.ogocx.authservice.usecases.UpdateCredentialUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserUpdatedConsumer {

    private final UpdateCredentialUseCase updateCredentialUseCase;

    @RabbitListener(queues = "${rabbitmq.queue.user.updated}")
    public void consume(UserUpdatedMessageDTO dto) {
        log.info("Received UserUpdatedEvent for userId: {}",dto.userId());
        updateCredentialUseCase.execute(dto);
    }
}
