package com.healthcore.authservice.interfaces.rest.patient;

import com.healthcore.authservice.application.patient.dto.request.InitiatePatientLoginRequest;
import com.healthcore.authservice.application.patient.dto.request.RegisterPatientRequest;
import com.healthcore.authservice.application.patient.dto.request.VerifyPatientOtpRequest;
import com.healthcore.authservice.application.patient.dto.response.TokenResponse;
import com.healthcore.authservice.application.patient.usecase.InitiatePatientLoginUseCase;
import com.healthcore.authservice.application.patient.usecase.RegisterPatientUseCase;
import com.healthcore.authservice.application.patient.usecase.VerifyPatientOtpUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth/patient")
@RequiredArgsConstructor
public class PatientAuthController {

    private final RegisterPatientUseCase registerUseCase;
    private final InitiatePatientLoginUseCase initiateLoginUseCase;
    private final VerifyPatientOtpUseCase verifyOtpUseCase;

    /**
     * POST /auth/patient/register
     * Optimized to handle the full registration record.
     */
    @PostMapping("/register")
    public Mono<ResponseEntity<String>> register(@Valid @RequestBody RegisterPatientRequest request) {
        // Passing the full record to the use case to be mapped to gRPC
        return registerUseCase.execute(request)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }

    /**
     * POST /auth/patient/login/initiate
     */
    @PostMapping("/login/initiate")
    public Mono<ResponseEntity<Void>> initiateLogin(@Valid @RequestBody InitiatePatientLoginRequest request) {
        // Use record accessor style: .phoneNumber() and .email()
        return initiateLoginUseCase.execute(request.phoneNumber(), request.email())
                .thenReturn(ResponseEntity.ok().build());
    }

    /**
     * POST /auth/patient/login/verify
     */
    @PostMapping("/login/verify")
    public Mono<ResponseEntity<TokenResponse>> verifyOtp(@Valid @RequestBody VerifyPatientOtpRequest request) {
        // Use record accessor style: .phoneNumber() and .otp()
        return verifyOtpUseCase.execute(request.phoneNumber(), request.otp())
                .map(ResponseEntity::ok);
    }
}