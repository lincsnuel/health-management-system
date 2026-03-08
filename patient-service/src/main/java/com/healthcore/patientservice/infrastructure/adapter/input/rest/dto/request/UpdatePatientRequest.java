package com.healthcore.patientservice.infrastructure.adapter.input.rest.dto.request;

import com.healthcore.patientservice.domain.model.enums.BloodGroup;
import com.healthcore.patientservice.domain.model.enums.Genotype;
import com.healthcore.patientservice.domain.model.enums.IdentityType;
import com.healthcore.patientservice.domain.model.enums.Religion;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

public record UpdatePatientRequest(

        String tenantId,
        @Email(message = "Email must be valid")
        @Size(max = 100)
        String email,

        @Pattern(
                regexp = "^(\\+234|0)(70|80|81|90|91)\\d{8}$",
                message = "Contact number must be a valid Nigerian phone number"
        )
        String contactNumber,

        BloodGroup bloodGroup,

        Genotype genotype,

        Religion religion,

        IdentityType identityType,

        @Size(max = 50)
        String nationalIdNumber,

        @Valid
        ResponsiblePartyRequest responsiblePartyRequest

) {

    @AssertTrue(message = "Both identityType and nationalIdNumber must be provided together")
    public boolean isNationalIdPairValid() {
        if (identityType == null && (nationalIdNumber == null || nationalIdNumber.isBlank())) {
            return true;
        }
        return identityType != null && nationalIdNumber != null && !nationalIdNumber.isBlank();
    }
}