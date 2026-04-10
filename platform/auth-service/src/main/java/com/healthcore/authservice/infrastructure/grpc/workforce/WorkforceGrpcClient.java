package com.healthcore.authservice.infrastructure.grpc.workforce;

import com.google.common.util.concurrent.ListenableFuture;
import com.healthcore.contracts.workforce.*;
import com.healthcore.authservice.application.staff.dto.request.RegisterStaffRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class WorkforceGrpcClient {

    private final WorkforceServiceGrpc.WorkforceServiceFutureStub workforceFutureStub;

    /**
     * Registers a staff member asynchronously via gRPC
     */
    public ListenableFuture<RegisterStaffProtoResponse> registerStaff(RegisterStaffRequest dto, String tenantId) {

        log.info("Sending async gRPC request to register staff: {}", dto.getEmail());

        RegisterStaffProtoRequest request = RegisterStaffProtoRequest.newBuilder()
                .setTenantId(tenantId)
                .setDepartmentId(dto.getDepartmentId())
                .setFirstName(dto.getFirstName())
                .setMiddleName(nullSafe(dto.getMiddleName()))
                .setLastName(dto.getLastName())
                .setEmail(dto.getEmail())
                .setPhoneNumber(nullSafe(dto.getPhoneNumber()))
                .setGender(normalize(dto.getGender()))
                .setDateOfBirth(mapDate(dto.getDateOfBirth()))
                .setStaffType(normalize(dto.getStaffType()))
                .setIdentityType(nullSafe(dto.getIdentityType()))
                .setIdentityNumber(nullSafe(dto.getIdentityNumber()))
                .addAllRoles(dto.getRoles() != null ? dto.getRoles() : List.of())
                .build();

        // Returns immediately with a ListenableFuture
        return workforceFutureStub.registerStaff(request);
    }

    /**
     * Maps LocalDate → Proto Date
     */
    private Date mapDate(LocalDate date) {
        if (date == null) return Date.newBuilder().build();

        return Date.newBuilder()
                .setYear(date.getYear())
                .setMonth(date.getMonthValue())
                .setDay(date.getDayOfMonth())
                .build();
    }

    /**
     * Prevents null values (proto3 doesn't like nulls)
     */
    private String nullSafe(String value) {
        return value == null ? "" : value;
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toUpperCase();
    }
}