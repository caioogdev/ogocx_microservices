package com.ogocx.userservice.exceptions;

import com.ogocx.servicelib.error.DomainException;
import org.springframework.http.HttpStatus;

public class DocumentAlreadyExistsException extends DomainException {
    public DocumentAlreadyExistsException(String document) {
        super(UserError.DOCUMENT_ALREADY_EXISTS, "Document " + document + "is already in use" );
    }
}
