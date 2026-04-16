package com.healthcore.workforceservice.staff.domain.model.staff;

import com.healthcore.workforceservice.shared.domain.vo.StaffId;
import com.healthcore.workforceservice.staff.domain.exception.DomainException;
import com.healthcore.workforceservice.staff.domain.model.vo.CredentialingId;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Credentialing {

    @Getter
    private final CredentialingId id;
    private final StaffId staffId;

    private final List<ProfessionalLicense> licenses = new ArrayList<>();

    public Credentialing(CredentialingId id, StaffId staffId) {
        this.id = id;
        this.staffId = staffId;
    }

    public void issueLicense(String number, String body, LocalDate expiry) {
        boolean exists = licenses.stream()
                .anyMatch(l -> l.getLicenseNumber().equalsIgnoreCase(number));

        if (exists) {
            throw new DomainException("Duplicate license");
        }

        licenses.add(ProfessionalLicense.create(number, body, expiry));
    }

    public void renewLicense(String number, LocalDate expiry) {
        find(number).renew(expiry);
    }

    public void revokeLicense(String number) {
        licenses.remove(find(number));
    }

    public boolean hasValidLicense() {
        return licenses.stream().anyMatch(ProfessionalLicense::isValid);
    }

    private ProfessionalLicense find(String number) {
        return licenses.stream()
                .filter(l -> l.getLicenseNumber().equalsIgnoreCase(number))
                .findFirst()
                .orElseThrow(() -> new DomainException("License not found"));
    }
}