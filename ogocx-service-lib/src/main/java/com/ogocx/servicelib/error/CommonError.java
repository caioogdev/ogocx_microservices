package com.ogocx.servicelib.error;

import org.springframework.http.HttpStatus;

public enum CommonError implements ErrorCode {
    VALIDATION_FAILED       (HttpStatus.BAD_REQUEST,           "Validation failed"),
    MALFORMED_REQUEST       (HttpStatus.BAD_REQUEST,           "Malformed request body"),
    INVALID_PARAMETER       (HttpStatus.BAD_REQUEST,           "Invalid request parameter"),
    RESOURCE_NOT_FOUND      (HttpStatus.NOT_FOUND,             "Resource not found"),
    METHOD_NOT_ALLOWED      (HttpStatus.METHOD_NOT_ALLOWED,    "Method not allowed"),
    UNAUTHORIZED            (HttpStatus.UNAUTHORIZED,          "Authentication required"),
    FORBIDDEN               (HttpStatus.FORBIDDEN,             "Access denied"),
    CONFLICT                (HttpStatus.CONFLICT,              "Resource conflict"),
    INTERNAL_ERROR          (HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error");

    private final HttpStatus status;
    private final String defaultMessage;

    CommonError(HttpStatus status, String defaultMessage) {
        this.status = status;
        this.defaultMessage = defaultMessage;
    }

    @Override public String code() { return name(); }
    @Override public HttpStatus status() { return status; }
    @Override public String defaultMessage() { return defaultMessage; }
}
