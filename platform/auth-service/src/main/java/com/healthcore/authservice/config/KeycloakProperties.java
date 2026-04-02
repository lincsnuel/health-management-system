package com.healthcore.authservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "keycloak")
public class KeycloakProperties {
    private String baseUrl;
    private String realm;
    private String clientId;     // Maps to keycloak.client-id
    private String clientSecret; // Maps to keycloak.client-secret

    private Admin admin = new Admin();

    @Data
    public static class Admin {
        private String clientId;     // Maps to keycloak.admin.client-id
        private String clientSecret; // Maps to keycloak.admin.client-secret
    }
}