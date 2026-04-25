package com.ogocx.authservice.mappers;

import com.ogocx.authservice.dtos.UserCreatedMessageDTO;
import com.ogocx.authservice.models.CredentialModel;
import org.springframework.stereotype.Component;

@Component
public class CredentialMapper {

    public CredentialModel UserCreatedMessage(UserCreatedMessageDTO dto) {
        CredentialModel credential = new CredentialModel();
        credential.setId(dto.userId());
        credential.setEmail(dto.email());
        credential.setPasswordHash(dto.passwordHash());
        return credential;
    }
}
