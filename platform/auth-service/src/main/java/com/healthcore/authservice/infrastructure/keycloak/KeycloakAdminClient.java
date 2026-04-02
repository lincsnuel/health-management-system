package com.healthcore.authservice.infrastructure.keycloak;

import com.healthcore.authservice.config.KeycloakProperties;
import com.healthcore.authservice.infrastructure.keycloak.dto.KeycloakUserRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class KeycloakAdminClient {

    private final WebClient adminWebClient;
    private final KeycloakProperties properties;

    public KeycloakAdminClient(@Qualifier("keycloakAdminWebClient")WebClient adminWebClient,
                               KeycloakProperties properties) {
        this.adminWebClient = adminWebClient;
        this.properties = properties;
    }

    public Mono<Void> createUser(KeycloakUserRequest request) {

        return adminWebClient.post()
                .uri(properties.getBaseUrl() + "/admin/realms/" + properties.getRealm() + "/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        response.bodyToMono(String.class).flatMap(body -> {
                            log.error("Keycloak Error Body: {}", body); // This will tell you EXACTLY why
                            return Mono.error(new RuntimeException("Keycloak API Error: " + body));
                        })
                )
                .bodyToMono(Void.class);
    }

    public Mono<Void> setPassword(String userId, String password) {

        var body = new HashMap<String, Object>();
        body.put("type", "password");
        body.put("value", password);
        body.put("temporary", false);

        return adminWebClient.put()
                .uri(properties.getBaseUrl() + "/admin/realms/" + properties.getRealm() + "/users/" + userId + "/reset-password")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Void.class);
    }

    public Mono<Void> enableUser(String userId) {

        var body = new HashMap<String, Object>();
        body.put("enabled", true);

        return adminWebClient.put()
                .uri(properties.getBaseUrl() + "/admin/realms/" + properties.getRealm() + "/users/" + userId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Void.class);
    }

    /**
     * Assign realm roles to a Keycloak user.
     * @param userId Keycloak user ID
     * @param roles List of realm roles
     */
    public Mono<Void> assignRoles(String userId, List<String> roles) {
        // Build role representations
        List<Map<String, String>> roleReps = roles.stream()
                .map(role -> Map.of(
                        "name", role
                ))
                .toList();

        // POST to /users/{id}/role-mappings/realm
        return adminWebClient.post()
                .uri(properties.getBaseUrl() + "/admin/realms/" + properties.getRealm() +
                        "/users/" + userId + "/role-mappings/realm")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(roleReps)
                .retrieve()
                .bodyToMono(Void.class);
    }
}