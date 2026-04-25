package com.ogocx.userservice.usecases;

import com.ogocx.userservice.dtos.CreateUserDTO;
import com.ogocx.userservice.dtos.GetUserDTO;
import com.ogocx.userservice.factories.UserFactory;
import com.ogocx.userservice.mappers.UserMapper;
import com.ogocx.userservice.messaging.UserEventPublisher;
import com.ogocx.userservice.repositories.UserRepository;
import com.ogocx.userservice.validators.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateUserUseCase {

    private final UserRepository userRepository;
    private final UserFactory userFactory;
    private final UserValidator userValidator;
    private final UserEventPublisher userEventPublisher;
    private final UserMapper userMapper;

    @Transactional
    public GetUserDTO execute(CreateUserDTO dto){
        userValidator.validateCreate(dto);
        var creator = userValidator.validateAndFindUser(dto.createdBy());

        var newUser = userRepository.save(userFactory.create(dto, creator));
        userEventPublisher.publishUserCreated(userFactory.createdMessage(newUser));

        return userMapper.GetUserMapper(newUser);
    }
}
