package com.healthcore.workforceservice.domain.model.staff;

import com.healthcore.workforceservice.domain.model.enums.LicenseStatus;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
public class ProfessionalLicense {

    private final String id;
    private final String licenseNumber;
    private final String issuingBody;

    private LocalDate expiryDate;
    private LicenseStatus status;

    // ======================
    // CONSTRUCTOR (PRIVATE)
    // ======================

    private ProfessionalLicense(
            String id,
            String licenseNumber,
            String issuingBody,
            LocalDate expiryDate,
            LicenseStatus status
    ) {
        this.id = id;
        this.licenseNumber = licenseNumber;
        this.issuingBody = issuingBody;
        this.expiryDate = expiryDate;
        this.status = status;
    }

    // ======================
    // FACTORIES
    // ======================

    public static ProfessionalLicense create(
            String licenseNumber,
            String issuingBody,
            LocalDate expiryDate
    ) {
        String id = UUID.randomUUID().toString();

        LicenseStatus status = expiryDate.isBefore(LocalDate.now())
                ? LicenseStatus.EXPIRED
                : LicenseStatus.VERIFIED;

        return new ProfessionalLicense(
                id,
                licenseNumber,
                issuingBody,
                expiryDate,
                status
        );
    }

    public static ProfessionalLicense reconstruct(
            String id,
            String licenseNumber,
            String issuingBody,
            LocalDate expiryDate,
            LicenseStatus status
    ) {
        return new ProfessionalLicense(
                id,
                licenseNumber,
                issuingBody,
                expiryDate,
                status
        );
    }

    // ======================
    // BEHAVIOR
    // ======================

    public boolean isValid() {
        return status == LicenseStatus.VERIFIED && !isExpired();
    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(expiryDate);
    }

    public void renew(LocalDate newExpiryDate) {
        this.expiryDate = newExpiryDate;
        this.status = LicenseStatus.VERIFIED;
    }
}