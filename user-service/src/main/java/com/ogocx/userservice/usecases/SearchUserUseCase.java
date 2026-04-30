package com.ogocx.userservice.usecases;

import com.ogocx.servicelib.dtos.PaginationDTO;
import com.ogocx.userservice.dtos.GetUserDTO;
import com.ogocx.userservice.mappers.UserMapper;
import com.ogocx.userservice.repositories.UserRepository;
import com.ogocx.userservice.specifications.UserSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class SearchUserUseCase {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public PaginationDTO<GetUserDTO> execute(
            String firstName,
            String lastName,
            String email,
            String firstDocument,
            String secondDocument,
            Boolean status,
            LocalDate birthDate,
            int page,
            int perPage
    ) {
        PageRequest pageable = PageRequest.of(page, perPage, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<GetUserDTO> result = userRepository
                .findAll(UserSpecification.filter(firstName, lastName, email, firstDocument, secondDocument, status, birthDate), pageable)
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