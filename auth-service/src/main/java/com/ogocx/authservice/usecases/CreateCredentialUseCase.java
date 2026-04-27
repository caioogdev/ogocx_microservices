package com.ogocx.authservice.usecases;

import com.ogocx.authservice.dtos.UserCreatedMessageDTO;
import com.ogocx.authservice.factories.CredentialFactory;
import com.ogocx.authservice.repositories.CredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateCredentialUseCase {

    private final CredentialRepository credentialRepository;
    private final CredentialFactory credentialMapper;

    @Transactional
    public void execute(UserCreatedMessageDTO dto){
        credentialRepository.save(credentialMapper.UserCreatedMessage(dto));
    }
}
