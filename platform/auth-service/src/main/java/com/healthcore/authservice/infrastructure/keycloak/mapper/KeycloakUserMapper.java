package com.healthcore.authservice.infrastructure.keycloak.mapper;

import com.healthcore.authservice.domain.model.UserType;
import com.healthcore.authservice.infrastructure.keycloak.dto.KeycloakUserRequest;

import java.util.HashMap;
import java.util.Map;

public class KeycloakUserMapper {

    public static KeycloakUserRequest toPatientUser(
            String username,
            String email,
            String firstName,
            String lastName,
            String patientId,
            String tenantId
    ) {

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("userId", patientId);
        attributes.put("tenantId", tenantId);
        attributes.put("userType", UserType.PATIENT.name());

        return KeycloakUserRequest.builder()
                .username(username)
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .enabled(true)
                .attributes(attributes)
                .build();
    }

    public static KeycloakUserRequest toStaffUser(
            String username,
            String email,
            String firstName,
            String lastName,
            String staffId,
            String tenantId
    ) {

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("userId", staffId);
        attributes.put("tenantId", tenantId);
        attributes.put("userType", UserType.STAFF.name());

        return KeycloakUserRequest.builder()
                .username(username)
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .enabled(false) // important for invite flow
                .attributes(attributes)
                .build();
    }
}