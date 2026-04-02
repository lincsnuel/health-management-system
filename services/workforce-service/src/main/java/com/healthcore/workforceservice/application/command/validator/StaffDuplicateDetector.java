package com.healthcore.workforceservice.application.command.validator;

import com.healthcore.workforceservice.application.command.model.RegisterStaffCommand;
import com.healthcore.workforceservice.application.exception.PotentialDuplicatePatientException;
import com.healthcore.workforceservice.domain.repository.StaffCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StaffDuplicateDetector implements CommandValidator<RegisterStaffCommand> {

    private final StaffCommandRepository staffRepository;

    @Override
    public void validate(RegisterStaffCommand command) {

        boolean exists =
                staffRepository.existsByTenantIdAndNameAndDateOfBirth(
                        command.tenantId(),
                        command.firstName(),
                        command.lastName(),
                        command.dateOfBirth()
                );

        if (exists) {
            throw new PotentialDuplicatePatientException(
                    command.firstName() + " " + command.lastName(),
                    command.dateOfBirth()
            );
        }
    }
}