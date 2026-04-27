package com.ogocx.userservice.validators;

import com.ogocx.userservice.dtos.CreateUserDTO;
import com.ogocx.userservice.dtos.UpdateUserDTO;
import com.ogocx.userservice.exceptions.DocumentAlreadyExistsException;
import com.ogocx.userservice.exceptions.EmailAlreadyExistsException;
import com.ogocx.userservice.exceptions.UserNotFoundException;
import com.ogocx.userservice.models.UserModel;
import com.ogocx.userservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;

    public UserModel validateAndFindUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId.toString()));
    }

    public void validateCreate(CreateUserDTO dto) {
        if (userRepository.existsByEmail(dto.email()))
            throw new EmailAlreadyExistsException(dto.email());

        if (userRepository.existsByFirstDocument(dto.firstDocument()))
            throw new DocumentAlreadyExistsException(dto.firstDocument());

        if (dto.secondDocument() != null && userRepository.existsBySecondDocument(dto.secondDocument()))
            throw new DocumentAlreadyExistsException(dto.secondDocument());
    }

    public void validateUpdate(UUID id, UpdateUserDTO dto) {
        if (dto.email() != null && userRepository.existsByEmailAndIdNot(dto.email(), id))
            throw new EmailAlreadyExistsException(dto.email());

        if (dto.firstDocument() != null && userRepository.existsByFirstDocumentAndIdNot(dto.firstDocument(), id))
            throw new DocumentAlreadyExistsException(dto.firstDocument());

        if (dto.secondDocument() != null && userRepository.existsBySecondDocumentAndIdNot(dto.secondDocument(), id))
            throw new DocumentAlreadyExistsException(dto.secondDocument());
    }

}
