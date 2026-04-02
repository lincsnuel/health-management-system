package com.healthcore.authservice.infrastructure.keycloak;

import com.healthcore.authservice.config.KeycloakProperties;
import com.healthcore.authservice.infrastructure.keycloak.dto.KeycloakTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class KeycloakTokenClient {

    private final WebClient webClient;
    private final KeycloakProperties properties;

    /**
     * Exchanges credentials for a token set.
     * Returns Mono instead of blocking the thread.
     */
    public Mono<KeycloakTokenResponse> getToken(String username, String password) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "password");
        form.add("client_id", properties.getClientId());
        form.add("client_secret", properties.getClientSecret());
        form.add("username", username);
        form.add("password", password);

        return executeTokenRequest(form);
    }

    /**
     * Refreshes an expired access token.
     */
    public Mono<KeycloakTokenResponse> refreshToken(String refreshToken) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "refresh_token");
        form.add("client_id", properties.getClientId());
        form.add("client_secret", properties.getClientSecret());
        form.add("refresh_token", refreshToken);

        return executeTokenRequest(form);
    }

    /**
     * Private helper to dry up the redundant WebClient logic.
     */
    private Mono<KeycloakTokenResponse> executeTokenRequest(MultiValueMap<String, String> form) {
        return webClient.post()
                .uri(properties.getBaseUrl() + "/realms/" + properties.getRealm() + "/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(form))
                .retrieve()
                .bodyToMono(KeycloakTokenResponse.class);
    }
}