package com.ogocx.authservice.audit.constants;

public final class AuditActions {
    private AuditActions() {}

    public static final String SIGN_IN = "SIGN_IN";
    public static final String TOKEN_REFRESHED = "TOKEN_REFRESHED";
    public static final String CREDENTIAL_CREATE = "CREDENTIAL_CREATE";
    public static final String CREDENTIAL_UPDATE = "CREDENTIAL_UPDATE";
    public static final String LOGOUT = "LOGOUT";

}