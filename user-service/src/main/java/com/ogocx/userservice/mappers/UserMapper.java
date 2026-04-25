package com.ogocx.userservice.mappers;

import com.ogocx.ogocxbus.events.user.UserCreatedEvent;
import com.ogocx.ogocxbus.events.user.UserUpdatedEvent;
import com.ogocx.userservice.dtos.GetUserDTO;
import com.ogocx.userservice.dtos.UserCreatedMessageDTO;
import com.ogocx.userservice.dtos.UserSummaryDTO;
import com.ogocx.userservice.dtos.UserUpdatedMessageDTO;
import com.ogocx.userservice.models.UserModel;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public static UserSummaryDTO UserSummaryMapper(UserModel user){
        return new UserSummaryDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );
    }

    public GetUserDTO GetUserMapper(UserModel user) {
        return new GetUserDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getFirstDocument(),
                user.getSecondDocument(),
                user.getBirthDate(),
                user.getEmail(),
                user.getStatus(),
                user.getCreatedAt(),
                UserSummaryMapper(user.getCreatedBy()),
                user.getUpdatedAt(),
                user.getUpdatedBy() != null ? UserSummaryMapper(user.getUpdatedBy()) : null
        );
    }

    public UserCreatedEvent UserCreatedMessageMapper(UserCreatedMessageDTO dto){
        return new UserCreatedEvent(dto.id(), dto.email(), dto.passwordHash());
    }

    public UserUpdatedEvent UserUpdatedMessageMapper(UserUpdatedMessageDTO dto) {
        return new UserUpdatedEvent(dto.id(), dto.email(), dto.passwordHash());
    }
}
