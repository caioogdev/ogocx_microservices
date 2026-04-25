package com.ogocx.userservice.exceptions;


import com.ogocx.servicelib.error.DomainException;

public class EmailAlreadyExistsException extends DomainException {

    public EmailAlreadyExistsException(String email) {
        super(UserError.EMAIL_ALREADY_EXISTS, "Email " + email + "is already in use");
    }
}
