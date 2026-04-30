package com.ogocx.userservice.exceptions;

import com.ogocx.servicelib.error.ErrorCode;
import org.springframework.http.HttpStatus;

public enum UserError implements ErrorCode {
    EMAIL_ALREADY_EXISTS        (HttpStatus.CONFLICT,    "Email is already in use"),
    DOCUMENT_ALREADY_EXISTS     (HttpStatus.CONFLICT,  "Document is already in use"),
    USER_NOT_FOUND              (HttpStatus.NOT_FOUND,   "User not found"),
    USER_ALREADY_INACTIVE       (HttpStatus.CONFLICT,  "User is already inactive");


    private final HttpStatus status;
    private final String defaultMessage;

    UserError(HttpStatus status, String defaultMessage) {
        this.status = status;
        this.defaultMessage = defaultMessage;
    }

    @Override public String code()           { return name(); }
    @Override public HttpStatus status()     { return status; }
    @Override public String defaultMessage() { return defaultMessage; }
}