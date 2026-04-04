package com.healthcore.authservice.interfaces.rest.staff;

import com.healthcore.authservice.application.common.dto.response.TokenResponse;
import com.healthcore.authservice.application.staff.dto.request.CompleteStaffRegistrationRequest;
import com.healthcore.authservice.application.staff.dto.request.RegisterStaffRequest;
import com.healthcore.authservice.application.staff.dto.request.StaffLoginRequest;
import com.healthcore.authservice.application.staff.usecase.CompleteStaffRegistrationUseCase;
import com.healthcore.authservice.application.staff.usecase.RegisterStaffUseCase;
import com.healthcore.authservice.application.staff.usecase.StaffLoginUseCase; // Added
import com.healthcore.authservice.common.context.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth/staff")
@RequiredArgsConstructor
public class StaffAuthController {

    private final RegisterStaffUseCase registerStaffUseCase;
    private final CompleteStaffRegistrationUseCase completeRegistrationUseCase;
    private final StaffLoginUseCase staffLoginUseCase; // Injected UseCase

    /**
     * HR registers staff.
     */
    @PostMapping("/register")
    public Mono<ResponseEntity<String>> registerStaff(@Valid @RequestBody RegisterStaffRequest request) {
        return registerStaffUseCase.execute(request)
                .map(ResponseEntity::ok);
    }

    /**
     * Staff completes registration (Accepts invite/sets password).
     */
    @PostMapping("/register/complete")
    public Mono<ResponseEntity<Void>> completeRegistration(@Valid @RequestBody CompleteStaffRegistrationRequest request) {
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
     * Now delegated to StaffLoginUseCase.
     */
    @PostMapping("/login")
    public Mono<ResponseEntity<TokenResponse>> login(@Valid @RequestBody StaffLoginRequest request) {
        return staffLoginUseCase.execute(request)
                .map(ResponseEntity::ok);
    }
}