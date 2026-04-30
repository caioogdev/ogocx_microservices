package com.ogocx.authservice.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncoderUtils {

    private final String pepper;

    public PasswordEncoderUtils(@Value("${security.pepper}") String pepper) {
        this.pepper = pepper;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Argon2PasswordEncoder(16, 32, 1, 65536, 3);
    }

    @Bean
    public PepperPasswordEncoderUtils pepperPasswordEncoder(PasswordEncoder passwordEncoder) {
        return new PepperPasswordEncoderUtils(passwordEncoder, pepper);
    }
}

