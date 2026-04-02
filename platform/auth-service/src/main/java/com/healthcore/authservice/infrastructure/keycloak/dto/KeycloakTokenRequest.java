package com.healthcore.authservice.infrastructure.keycloak.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KeycloakTokenRequest {
    private String username;
    private String password;
    private String grantType;
    private String refreshToken;
}