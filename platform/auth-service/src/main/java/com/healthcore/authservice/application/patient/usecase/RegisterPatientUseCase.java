package com.healthcore.authservice.application.patient.usecase;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.healthcore.authservice.application.patient.dto.request.RegisterPatientRequest;
import com.healthcore.authservice.common.context.TenantContext;
import com.healthcore.authservice.common.util.UsernameBuilder;
import com.healthcore.authservice.infrastructure.grpc.patient.PatientGrpcClient;
import com.healthcore.authservice.infrastructure.keycloak.KeycloakAdminClient;
import com.healthcore.authservice.infrastructure.keycloak.mapper.KeycloakUserMapper;
import com.healthcore.contracts.patient.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterPatientUseCase {

    private final PatientGrpcClient patientClient;
    private final KeycloakAdminClient keycloakAdminClient;

    /**
     * Registers a Patient reactively with full profile data
     */
    public Mono<String> execute(RegisterPatientRequest request) {
        // 1. Start by retrieving the tenantId from the reactive context
        return TenantContext.getTenantId()
                .switchIfEmpty(Mono.error(new IllegalStateException("Tenant ID missing from context")))
                .flatMap(tenantId -> {

                    // 2. Map Record to gRPC Message using the retrieved tenantId
                    var grpcRequest = mapToGrpcRequest(request, tenantId);

                    // 3. Execute gRPC call and convert ListenableFuture to Mono
                    return toMono(patientClient.registerPatient(grpcRequest))
                            .flatMap(response -> {
                                if (!response.getSuccess()) {
                                    return Mono.error(new RuntimeException("Patient Service Error: " + response.getMessage()));
                                }

                                String patientId = response.getPatientId();
                                // Use the tenantId from our outer scope
                                String username = UsernameBuilder.build(request.contactNumber(), tenantId);

                                // 4. Map to Keycloak User (Using the new userId + userType pattern)
                                var kcUser = KeycloakUserMapper.toPatientUser(
                                        username,
                                        request.email(),
                                        request.firstName(),
                                        request.lastName(),
                                        patientId,
                                        tenantId
                                );

                                // 5. Create user in Keycloak and return the patientId
                                return keycloakAdminClient.createUser(kcUser)
//                                        .then(keycloakAdminClient.assignRoles(username, request.roles()))
                                        .thenReturn(patientId);
                            });
                })
                .doOnError(e -> log.error("Patient registration failed for phone {}: {}",
                        request.contactNumber(), e.getMessage()));
    }

    // --- Helper Methods remain largely the same but ensure they are called within the reactive chain ---

    private RegisterPatientProtoRequest mapToGrpcRequest(RegisterPatientRequest req, String tenantId) {
        var builder = RegisterPatientProtoRequest.newBuilder()
                .setHospitalPatientNumber(req.hospitalPatientNumber())
                .setFirstName(req.firstName())
                .setLastName(req.lastName())
                .setDateOfBirth(toDateMessage(req.dateOfBirth()))
                .setGender(normalize(req.gender()))
                .setEmail(req.email() != null ? req.email() : "")
                .setContactNumber(req.contactNumber())
                .setTenantId(tenantId);

        if (req.identityType() != null) builder.setIdentityType(req.identityType());
        if (req.nationalIdNumber() != null) builder.setNationalIdNumber(req.nationalIdNumber());

        if (req.address() != null) {
            builder.setAddress(Address.newBuilder()
                    .setStreet(req.address().street())
                    .setCity(req.address().city())
                    .setState(req.address().state())
                    .setCountry(req.address().country())
                    .build());
        }

        if (req.responsibleParties() != null) {
            builder.addAllResponsibleParties(req.responsibleParties().stream()
                    .map(rp -> {
                        assert req.address() != null;
                        return ResponsibleParty.newBuilder()
                                .setFirstName(rp.firstName())
                                .setLastName(rp.lastName())
                                .setType(normalize(rp.type()))
                                .setRelationship(rp.relationship())
                                .setContactNumber(rp.contactNumber())
                                .setAddress(
                                        Address.newBuilder()
                                                .setStreet(req.address().street())
                                                .setCity(req.address().city())
                                                .setState(req.address().state())
                                                .setCountry(req.address().country())
                                                .build()
                                )
                                .build();
                    })
                    .collect(Collectors.toList()));
        }

        if (req.insurancePolicy() != null) {
            var ins = req.insurancePolicy();
            builder.setInsurancePolicy(InsurancePolicy.newBuilder()
                    .setProviderName(ins.providerName())
                    .setPolicyNumber(ins.policyNumber())
                    .setPlanType(ins.planType() != null ? ins.planType() : "")
                    .setCoverageStart(toDateMessage(ins.coverageStart()))
                    .setCoverageEnd(toDateMessage(ins.coverageEnd()))
                    .setMain(ins.main() != null && ins.main())
                    .build());
        }

        return builder.build();
    }

    private Date toDateMessage(LocalDate date) {
        if (date == null) return Date.getDefaultInstance();
        return Date.newBuilder()
                .setYear(date.getYear())
                .setMonth(date.getMonthValue())
                .setDay(date.getDayOfMonth())
                .build();
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toUpperCase();
    }

    private <T> Mono<T> toMono(ListenableFuture<T> future) {
        return Mono.create(sink ->
                Futures.addCallback(future, new FutureCallback<>() {
                    @Override
                    public void onSuccess(T result) {
                        sink.success(result);
                    }

                    @Override
                    public void onFailure(@NonNull Throwable t) {
                        sink.error(t);
                    }
                }, MoreExecutors.directExecutor())
        );
    }
}