package com.healthcore.workforceservice.staff.domain.model.staff;

import com.healthcore.workforceservice.shared.domain.vo.StaffId;
import com.healthcore.workforceservice.staff.domain.exception.LicenseNotFoundException;
import com.healthcore.workforceservice.staff.domain.model.vo.CredentialingId;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Credentialing {

    private final CredentialingId id;
    private final StaffId staffId;

    private final List<ProfessionalLicense> licenses = new ArrayList<>();

    public Credentialing(CredentialingId id, StaffId staffId) {
        this.id = id;
        this.staffId = staffId;
    }

    public void loadLicense(ProfessionalLicense license) {
        this.licenses.add(license);
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
                .orElseThrow(() -> new LicenseNotFoundException("License not found"));
    }
}