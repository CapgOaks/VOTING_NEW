package com.capgemini.security4.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggingPasswordEncoder implements PasswordEncoder {

    private final PasswordEncoder delegate = new BCryptPasswordEncoder();

    @Override
    public String encode(CharSequence rawPassword) {
        String encoded = delegate.encode(rawPassword);
        log.info("Encoding password: raw='{}', encoded='{}'", rawPassword, encoded);
        return encoded;
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        boolean matches = delegate.matches(rawPassword, encodedPassword);
        log.info("Matching password: raw='{}', encoded='{}', result={}", rawPassword, encodedPassword, matches);
        return matches;
    }
}
