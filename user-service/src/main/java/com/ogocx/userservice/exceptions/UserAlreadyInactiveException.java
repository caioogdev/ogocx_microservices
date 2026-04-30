package com.ogocx.userservice.exceptions;

import com.ogocx.servicelib.error.DomainException;

public class UserAlreadyInactiveException extends DomainException {
    public UserAlreadyInactiveException(){
        super(UserError.USER_ALREADY_INACTIVE);
    }
}
