package com.healthcore.patientservice.application.command.model;

import com.healthcore.patientservice.application.query.model.AddressDetail;
import com.healthcore.patientservice.application.query.model.InsuranceDetail;
import com.healthcore.patientservice.application.query.model.ResponsiblePartyDetail;
import com.healthcore.patientservice.domain.model.enums.BloodGroup;
import com.healthcore.patientservice.domain.model.enums.Genotype;
import com.healthcore.patientservice.domain.model.enums.IdentityType;
import com.healthcore.patientservice.domain.model.enums.Religion;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Command to update patient details.
 * Allows partial updates: only non-null fields will be applied.
 */
public record UpdatePatientCommand(

        UUID patientId,

        // Contact
        String email,
        String contactNumber,

        // Clinical profile
        BloodGroup bloodGroup,
        Genotype genotype,

        // Demographics
        Religion religion,

        // National ID
        IdentityType identityType,
        String nationalIdNumber,

        // Nested aggregates (optional)
        List<ResponsiblePartyDetail> responsibleParties,
        List<AddressDetail> addresses,
        List<InsuranceDetail> insurances

) {

    // -------------------- Helper Methods --------------------

    public Optional<String> emailOpt() {
        return Optional.ofNullable(email);
    }

    public Optional<String> contactNumberOpt() {
        return Optional.ofNullable(contactNumber);
    }

    public Optional<BloodGroup> bloodGroupOpt() {
        return Optional.ofNullable(bloodGroup);
    }

    public Optional<Genotype> genotypeOpt() {
        return Optional.ofNullable(genotype);
    }

    public Optional<Religion> religionOpt() {
        return Optional.ofNullable(religion);
    }

    public Optional<IdentityType> identityTypeOpt() {
        return Optional.ofNullable(identityType);
    }

    public Optional<String> nationalIdNumberOpt() {
        return Optional.ofNullable(nationalIdNumber);
    }

    public Optional<List<ResponsiblePartyDetail>> responsiblePartiesOpt() {
        return Optional.ofNullable(responsibleParties);
    }

    public Optional<List<AddressDetail>> addressesOpt() {
        return Optional.ofNullable(addresses);
    }

    public Optional<List<InsuranceDetail>> insurancesOpt() {
        return Optional.ofNullable(insurances);
    }

}