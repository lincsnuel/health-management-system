package com.healthcore.workforceservice.staff.application.command.validator;

import com.healthcore.workforceservice.staff.application.command.model.RegisterStaffCommand;
import com.healthcore.workforceservice.staff.application.exception.PotentialDuplicatePatientException;
import com.healthcore.workforceservice.staff.domain.repository.StaffCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StaffDuplicateDetector implements CommandValidator<RegisterStaffCommand> {

    private final StaffCommandRepository staffRepository;

    @Override
    public void validate(RegisterStaffCommand command) {

        boolean exists =
                staffRepository.existsByNameAndDateOfBirth(
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