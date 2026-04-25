package com.ogocx.authservice.exceptions;

import com.ogocx.servicelib.error.DomainException;
import org.springframework.http.HttpStatus;

public class InvalidCredentials extends DomainException {
    public InvalidCredentials() {
        super(AuthError.INVALID_CREDENTIALS, "Invalid Credentials");
    }
}
