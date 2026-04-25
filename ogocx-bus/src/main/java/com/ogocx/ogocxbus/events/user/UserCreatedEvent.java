package com.ogocx.ogocxbus.events.user;

import com.ogocx.ogocxbus.base.DomainEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class UserCreatedEvent extends DomainEvent {
    private UUID userId;
    private String email;
    private String passwordHash;

    public UserCreatedEvent(UUID userId, String email, String passwordHash) {
        super();
        this.userId = userId;
        this.email = email;
        this.passwordHash = passwordHash;
    }
}
