package com.ogocx.userservice.usecases;

import com.ogocx.userservice.dtos.GetUserDTO;
import com.ogocx.userservice.dtos.UpdateUserDTO;
import com.ogocx.userservice.exceptions.UserNotFoundException;
import com.ogocx.userservice.factories.UserFactory;
import com.ogocx.userservice.mappers.UserMapper;
import com.ogocx.userservice.messaging.UserEventPublisher;
import com.ogocx.userservice.models.UserModel;
import com.ogocx.userservice.repositories.UserRepository;
import com.ogocx.userservice.validators.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateUserUseCase {

    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final UserFactory userFactory;
    private final UserMapper userMapper;
    private final UserEventPublisher userEventPublisher;


    @Transactional
    public GetUserDTO execute(UUID id, UpdateUserDTO dto) {
        UserModel user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id.toString()));

        userValidator.validateUpdate(id, dto);
        var updater = userValidator.validateAndFindUser(dto.updatedBy());
        userFactory.update(user, dto, updater);

        var updatedUser = userRepository.save(user);
        userEventPublisher.publishUserUpdated(userFactory.updatedMessage(updatedUser));

        return userMapper.GetUserMapper(updatedUser);
    }
}
