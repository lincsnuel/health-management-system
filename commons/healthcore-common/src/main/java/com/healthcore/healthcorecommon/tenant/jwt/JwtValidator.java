package com.healthcore.healthcorecommon.tenant.jwt;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;

@Component
public class JwtValidator {

    private final JwtDecoder jwtDecoder;

    public JwtValidator(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    public Jwt validate(String token) {
        return jwtDecoder.decode(token);
    }
}