package com.ogocx.userservice.usecases;

import com.ogocx.userservice.exceptions.UserAlreadyInactiveException;
import com.ogocx.userservice.factories.UserFactory;
import com.ogocx.userservice.repositories.UserRepository;
import com.ogocx.userservice.validators.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeactivateUserUseCase {

    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final UserFactory userFactory;

    @Transactional
    public void execute(UUID id){
        var user = userValidator.findById(id);

        if(!user.getStatus()) throw new UserAlreadyInactiveException();

        userFactory.desactive(user);
        userRepository.save(user);
    }
}
