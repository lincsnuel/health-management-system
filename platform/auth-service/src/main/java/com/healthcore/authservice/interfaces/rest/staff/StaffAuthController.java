package com.healthcore.authservice.interfaces.rest.staff;

import com.healthcore.authservice.application.patient.dto.response.TokenResponse;
import com.healthcore.authservice.application.staff.dto.request.CompleteStaffRegistrationRequest;
import com.healthcore.authservice.application.staff.dto.request.RegisterStaffRequest;
import com.healthcore.authservice.application.staff.dto.request.StaffLoginRequest;
import com.healthcore.authservice.application.staff.usecase.CompleteStaffRegistrationUseCase;
import com.healthcore.authservice.application.staff.usecase.RegisterStaffUseCase;
import com.healthcore.authservice.common.context.TenantContext;
import com.healthcore.authservice.common.util.UsernameBuilder;
import com.healthcore.authservice.infrastructure.keycloak.KeycloakTokenClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth/staff")
@RequiredArgsConstructor
public class StaffAuthController {

    private final RegisterStaffUseCase registerStaffUseCase;
    private final CompleteStaffRegistrationUseCase completeRegistrationUseCase;
    private final KeycloakTokenClient tokenClient;

    /**
     * HR registers staff.
     * The UseCase itself will pull the TenantContext.
     */
    @PostMapping("/register")
    public Mono<ResponseEntity<String>> registerStaff(@RequestBody RegisterStaffRequest request) {
        return registerStaffUseCase.execute(request)
                .map(ResponseEntity::ok);
    }

    /**
     * Staff completes registration (Accepts invite/sets password).
     */
    @PostMapping("/register/complete")
    public Mono<ResponseEntity<Void>> completeRegistration(@RequestBody CompleteStaffRegistrationRequest request) {
        // Chain the context lookup so the tenantId is available for the use case
        return TenantContext.getTenantId()
                .switchIfEmpty(Mono.error(new IllegalStateException("Tenant ID missing")))
                .flatMap(tenantId -> completeRegistrationUseCase.execute(
                        request.getToken(),
                        tenantId,
                        request.getPassword()
                ))
                .thenReturn(ResponseEntity.ok().build());
    }

    /**
     * Staff login.
     */
    @PostMapping("/login")
    public Mono<ResponseEntity<TokenResponse>> login(@RequestBody StaffLoginRequest request) {
        // Use flatMap to get the tenantId before building the username
        return TenantContext.getTenantId()
                .switchIfEmpty(Mono.error(new IllegalStateException("Tenant ID missing")))
                .flatMap(tenantId -> {
                    String username = UsernameBuilder.build(request.getEmail(), tenantId);

                    return tokenClient.getToken(username, request.getPassword())
                            .map(kcToken -> {
                                TokenResponse response = new TokenResponse();
                                response.setAccessToken(kcToken.getAccessToken());
                                response.setRefreshToken(kcToken.getRefreshToken());
                                response.setExpiresIn(kcToken.getExpiresIn());
                                return ResponseEntity.ok(response);
                            });
                });
    }
}