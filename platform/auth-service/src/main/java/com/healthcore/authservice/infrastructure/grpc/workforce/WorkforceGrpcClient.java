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

@Slf4j
@Component
@RequiredArgsConstructor
public class WorkforceGrpcClient {

    private final WorkforceServiceGrpc.WorkforceServiceFutureStub baseStub;

    /**
     * Registers a staff member asynchronously via gRPC.
     * Maps the internal RegisterStaffRequest DTO to the gRPC RegisterStaffProtoRequest.
     */
    public ListenableFuture<RegisterStaffProtoResponse> registerStaff(
            RegisterStaffRequest dto,
            String tenantId,
            String currentUserId
    ) {
        log.info("Preparing async gRPC request to register staff: {} in department: {}",
                dto.getEmail(), dto.getDepartmentId());

        // Create a scoped stub with security and multi-tenancy headers
        var stub = attachContext(tenantId, currentUserId, null, null);

        // Build the Protobuf request using the updated fields
        RegisterStaffProtoRequest request = RegisterStaffProtoRequest.newBuilder()
                // Organization Context
                .setDepartmentId(nullSafe(dto.getDepartmentId()))

                // Personal Identity
                .setFirstName(nullSafe(dto.getFirstName()))
                .setMiddleName(nullSafe(dto.getMiddleName()))
                .setLastName(nullSafe(dto.getLastName()))
                .setEmail(nullSafe(dto.getEmail()))
                .setPhoneNumber(nullSafe(dto.getPhoneNumber()))
                .setGender(normalize(dto.getGender()))
                .setDateOfBirth(mapDate(dto.getDateOfBirth()))

                // Staff Classification
                .setStaffType(normalize(dto.getStaffType()))

                // Employment Details
                .setEmployeeId(nullSafe(dto.getEmployeeId()))
                .setEmploymentType(normalize(dto.getEmploymentType()))
                .setDateOfHire(mapDate(dto.getDateOfHire()))

                // Professional Profile
                .setSpecialization(nullSafe(dto.getSpecialization()))
                .setAcademicTitle(nullSafe(dto.getAcademicTitle()))
                .setIsConsultant(dto.isConsultant())

                // Access Control
                .addAllRoles(dto.getRoles() != null ? dto.getRoles() : List.of())
                .build();

        return stub.registerStaff(request);
    }

    /**
     * Attaches metadata headers to the stub for context propagation.
     */
    private WorkforceServiceGrpc.WorkforceServiceFutureStub attachContext(
            String tenantId,
            String userId,
            String userType,
            String roles
    ) {
        Metadata metadata = new Metadata();

        if (tenantId != null) metadata.put(TENANT_ID, tenantId);
        if (userId != null) metadata.put(USER_ID, userId);
        if (userType != null) metadata.put(USER_TYPE, userType);
        if (roles != null) metadata.put(ROLES, roles);

        return baseStub.withInterceptors(MetadataUtils.newAttachHeadersInterceptor(metadata));
    }

    /**
     * Maps Java LocalDate to Proto Date message.
     */
    private Date mapDate(LocalDate date) {
        if (date == null) {
            return Date.getDefaultInstance();
        }

        return Date.newBuilder()
                .setYear(date.getYear())
                .setMonth(date.getMonthValue())
                .setDay(date.getDayOfMonth())
                .build();
    }

    /**
     * Protobuf 3 does not support null strings; they must be empty strings.
     */
    private String nullSafe(String value) {
        return value == null ? "" : value;
    }

    /**
     * Normalizes string values for Enums/Lookups (Trim and Uppercase).
     */
    private String normalize(String value) {
        return value == null ? "" : value.trim().toUpperCase();
    }
}