package com.ogocx.authservice.usecases;

import com.ogocx.authservice.dtos.UserCreatedMessageDTO;
import com.ogocx.authservice.mappers.CredentialMapper;
import com.ogocx.authservice.repositories.CredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCredentialUseCase {

    private final CredentialRepository credentialRepository;
    private final CredentialMapper credentialMapper;

    public void execute(UserCreatedMessageDTO dto){
        credentialRepository.save(credentialMapper.UserCreatedMessage(dto));
    }
}
