package com.healthcore.tenantservice.domain.model.vo;

import com.healthcore.tenantservice.domain.exception.InvalidDataRetentionPolicyException;

public record DataRetentionPolicy(
        Integer patientRecordsRetentionYears,
        Integer auditLogsRetentionYears
) {

    public DataRetentionPolicy {
        try {
            patientRecordsRetentionYears = normalize(patientRecordsRetentionYears, "patientRecordsRetentionYears");
            auditLogsRetentionYears = normalize(auditLogsRetentionYears, "auditLogsRetentionYears");
        } catch (InvalidDataRetentionPolicyException e) {
            throw new InvalidDataRetentionPolicyException(e);
        }
    }

    private static Integer normalize(Integer value, String fieldName) throws InvalidDataRetentionPolicyException {
        if (value == null) {
            throw new InvalidDataRetentionPolicyException(fieldName + " must not be null");
        }

        if (value <= 0) {
            throw new InvalidDataRetentionPolicyException(fieldName + " must be greater than 0");
        }

        if (value > 50) { // domain constraint to prevent unrealistic retention
            throw new InvalidDataRetentionPolicyException(fieldName + " must not exceed 50 years");
        }

        return value;
    }
}