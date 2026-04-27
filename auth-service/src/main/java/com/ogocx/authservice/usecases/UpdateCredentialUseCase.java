package com.ogocx.authservice.usecases;

import com.ogocx.authservice.dtos.UserUpdatedMessageDTO;
import com.ogocx.authservice.repositories.CredentialRepository;
import com.ogocx.authservice.validators.AuthValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateCredentialUseCase {

    private final CredentialRepository credentialRepository;
    private final AuthValidator authValidator;

    @Transactional
    public void execute(UserUpdatedMessageDTO dto) {
        var credential = authValidator.findById(dto.userId());

        if (dto.email() != null) credential.setEmail(dto.email());
        if (dto.passwordHash() != null) credential.setPasswordHash(dto.passwordHash());

        credentialRepository.save(credential);
    }
}
