package com.ogocx.userservice.usecases;

import com.ogocx.userservice.dtos.GetUserDTO;
import com.ogocx.userservice.mappers.UserMapper;
import com.ogocx.userservice.repositories.UserRepository;
import com.ogocx.servicelib.dtos.PaginationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetAllUsersUseCase {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public PaginationDTO<GetUserDTO> execute(int page, int perPage) {
        PageRequest pageable = PageRequest.of(page, perPage, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<GetUserDTO> result = userRepository.findAll(pageable)
                .map(userMapper::GetUserMapper);

        return new PaginationDTO<>(
                result.getTotalElements(),
                result.getContent(),
                result.getNumber(),
                result.getSize(),
                result.hasNext(),
                result.hasPrevious()
        );
    }
}
