package com.ogocx.userservice.exceptions;

import com.ogocx.servicelib.error.DomainException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends DomainException {
    public UserNotFoundException(String id){
        super(UserError.USER_NOT_FOUND, "User with id " + id + " not found" );
    }
}
