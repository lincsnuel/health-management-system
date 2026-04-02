package com.healthcore.authservice.infrastructure.keycloak.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class KeycloakUserRequest {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private boolean enabled;
    private Map<String, Object> attributes;
}