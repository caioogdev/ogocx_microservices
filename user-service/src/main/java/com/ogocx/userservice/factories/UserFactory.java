package com.ogocx.userservice.factories;

import com.ogocx.userservice.dtos.*;
import com.ogocx.userservice.models.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserFactory {

    private final PasswordEncoder passwordEncoder;

    public UserModel create(CreateUserDTO dto, UserModel createdBy){
        UserModel user = new UserModel();
        user.setFirstName(dto.firstName().trim());
        user.setLastName(dto.lastName().trim());
        user.setFirstDocument(dto.firstDocument().trim());
        user.setSecondDocument(dto.secondDocument() != null ? dto.secondDocument().trim() : null);
        user.setBirthDate(dto.birthDate());
        user.setEmail(dto.email().trim().toLowerCase());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setStatus(dto.status());
        user.setCreatedBy(createdBy);
        return user;
    }

    public void update(UserModel user, UpdateUserDTO dto, UserModel updatedBy){
        if (dto.firstName() != null) user.setFirstName(dto.firstName().trim());
        if (dto.lastName() != null) user.setLastName(dto.lastName().trim());
        if (dto.firstDocument() != null) user.setFirstDocument(dto.firstDocument().trim());
        if (dto.secondDocument() != null) user.setSecondDocument(dto.secondDocument().trim());
        if (dto.birthDate() != null) user.setBirthDate(dto.birthDate());
        if (dto.email() != null) user.setEmail(dto.email().trim().toLowerCase());
        if (dto.status() != null) user.setStatus(dto.status());
        user.setUpdatedAt(LocalDateTime.now());
        user.setUpdatedBy(updatedBy);
    }

    public UserCreatedMessageDTO createdMessage(UserModel user) {
        return new UserCreatedMessageDTO(
                user.getId(),
                user.getEmail(),
                user.getPassword()
        );
    }

    public UserUpdatedMessageDTO updatedMessage(UserModel user) {
        return new UserUpdatedMessageDTO(
                user.getId(),
                user.getEmail(),
                user.getPassword()
        );
    }
}
