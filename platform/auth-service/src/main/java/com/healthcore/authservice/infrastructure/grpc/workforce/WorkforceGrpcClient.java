package com.healthcore.authservice.infrastructure.grpc.workforce;

import com.google.common.util.concurrent.ListenableFuture;
import com.healthcore.contracts.workforce.*;
import com.healthcore.authservice.application.staff.dto.request.RegisterStaffRequest;
import io.grpc.Metadata;
import io.grpc.stub.MetadataUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

import static com.healthcore.authservice.infrastructure.grpc.common.Header.*;
import static com.healthcore.authservice.infrastructure.grpc.common.Header.ROLES;

@Slf4j
@Component
@RequiredArgsConstructor
public class WorkforceGrpcClient {

    private final WorkforceServiceGrpc.WorkforceServiceFutureStub workforceFutureStub;

    private final WorkforceServiceGrpc.WorkforceServiceFutureStub baseStub;


    /**
     * Creates a new header interceptor and applies it to the baseStub.
     */
    private WorkforceServiceGrpc.WorkforceServiceFutureStub attachContext(
            String tenantId,
            String userId,
            String userType,
            String roles
    ) {
        Metadata metadata = new Metadata();

        // Tenant ID is mandatory for multi-tenant data isolation
        if (tenantId != null) {
            metadata.put(TENANT_ID, tenantId);
        }

        // Optional security context fields
        if (userId != null) metadata.put(USER_ID, userId);
        if (userType != null) metadata.put(USER_TYPE, userType);
        if (roles != null) metadata.put(ROLES, roles);

        /* * FIX: MetadataUtils.newAttachHeadersInterceptor creates a ClientInterceptor.
         * We then use baseStub.withInterceptors() to return a new stub instance
         * that will include these headers in the call.
         */
        return baseStub.withInterceptors(MetadataUtils.newAttachHeadersInterceptor(metadata));
    }

    /**
     * Registers a staff member asynchronously via gRPC
     */
    public ListenableFuture<RegisterStaffProtoResponse> registerStaff(
            RegisterStaffRequest dto,
            String tenantId,
            String staffId
    ) {

        log.info("Sending async gRPC request to register staff: {}", dto.getEmail());

        // Create a scoped stub with tenant headers
        var stub = attachContext(tenantId, staffId, null, null);

        RegisterStaffProtoRequest request = RegisterStaffProtoRequest.newBuilder()
                .setStaffId(staffId)
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
        return stub.registerStaff(request);
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