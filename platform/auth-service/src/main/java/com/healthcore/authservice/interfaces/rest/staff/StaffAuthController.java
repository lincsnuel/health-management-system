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
import org.springframework.http.HttpStatus;
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
    private final StaffLoginUseCase staffLoginUseCase;

    @PostMapping("/register")
    public Mono<ResponseEntity<String>> registerStaff(@Valid @RequestBody RegisterStaffRequest request) {
        System.out.println("Inside registerStaff");
        return registerStaffUseCase.execute(request)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }

    @PostMapping("/register/complete")
    public Mono<ResponseEntity<TokenResponse>> completeRegistration(@Valid @RequestBody CompleteStaffRegistrationRequest request) {
        return TenantContext.getTenantId()
                .flatMap(tenantId -> completeRegistrationUseCase.execute(
                        request.getEmail(),
                        tenantId,
                        request.getPassword()
                ))
                .map(ResponseEntity::ok);
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<TokenResponse>> login(@Valid @RequestBody StaffLoginRequest request) {
        // Metadata is handled by MetadataWebFilter automatically
        return staffLoginUseCase.execute(request)
                .map(ResponseEntity::ok);
    }
}