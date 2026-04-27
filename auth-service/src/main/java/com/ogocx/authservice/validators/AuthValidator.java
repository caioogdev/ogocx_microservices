package com.ogocx.authservice.validators;

import com.ogocx.authservice.exceptions.InvalidCredentials;
import com.ogocx.authservice.models.CredentialModel;
import com.ogocx.authservice.repositories.CredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuthValidator {

    private final CredentialRepository credentialRepository;

    public CredentialModel findByEmail(String email){
        return credentialRepository.findByEmail(email)
                .orElseThrow((InvalidCredentials::new));
    }

    public CredentialModel findById(UUID id){
        return credentialRepository.findById(id)
                .orElseThrow((InvalidCredentials::new));
    }
}
