package com.healthcore.tenantservice.infrastructure.adapter.output.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class DataRetentionPolicyEntity {

    @Column(name = "patient_records_retention_years", nullable = false)
    private Integer patientRecordsRetentionYears;

    @Column(name = "audit_logs_retention_years", nullable = false)
    private Integer auditLogsRetentionYears;

    /* ================= HELPERS ================= */
    public void updatePatientRecordsRetentionYears(Integer years) {
        this.patientRecordsRetentionYears = years;
    }

    public void updateAuditLogsRetentionYears(Integer years) {
        this.auditLogsRetentionYears = years;
    }
}