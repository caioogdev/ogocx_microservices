package com.ogocx.userservice.usecases;

import com.ogocx.userservice.dtos.GetUserDTO;
import com.ogocx.userservice.exceptions.UserNotFoundException;
import com.ogocx.userservice.mappers.UserMapper;
import com.ogocx.userservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetUserUseCase {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public GetUserDTO execute(UUID id){
        return userRepository.findById(id)
                .map(userMapper::GetUserMapper)
                .orElseThrow(() -> new UserNotFoundException(id.toString()));
    }
}
