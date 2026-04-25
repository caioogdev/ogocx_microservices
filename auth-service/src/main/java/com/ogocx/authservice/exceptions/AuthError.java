package com.ogocx.authservice.exceptions;

import com.ogocx.servicelib.error.ErrorCode;
import org.springframework.http.HttpStatus;

public enum AuthError implements ErrorCode {
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "Invalid credentials");

    private final HttpStatus status;
    private final String defaultMessage;

    AuthError(HttpStatus status, String defaultMessage) {
        this.status = status;
        this.defaultMessage = defaultMessage;
    }

    @Override public String code()           { return name(); }
    @Override public HttpStatus status()     { return status; }
    @Override public String defaultMessage() { return defaultMessage; }
}
