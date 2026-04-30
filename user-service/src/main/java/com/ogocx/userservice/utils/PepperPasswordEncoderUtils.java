package com.ogocx.userservice.utils;

import org.springframework.security.crypto.password.PasswordEncoder;

public class PepperPasswordEncoderUtils {

    private final PasswordEncoder encoder;
    private final String pepper;

    public PepperPasswordEncoderUtils(PasswordEncoder encoder, String pepper) {
        this.encoder = encoder;
        this.pepper = pepper;
    }

    public String encode(String rawPassword) {
        return encoder.encode(rawPassword + pepper);
    }

    public boolean matches(String rawPassword, String hash) {
        return encoder.matches(rawPassword + pepper, hash);
    }
}
