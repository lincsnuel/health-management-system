package com.healthcore.patientservice.application.command.validator;

import com.healthcore.patientservice.application.command.model.RegisterPatientCommand;
import com.healthcore.patientservice.application.command.repository.PatientCommandRepository;
import com.healthcore.patientservice.application.exception.PotentialDuplicatePatientException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PatientDuplicateDetector implements CommandValidator<RegisterPatientCommand> {

    private final PatientCommandRepository patientRepository;

    @Override
    public void validate(RegisterPatientCommand command) {

        boolean exists =
                patientRepository.existsByTenantIdAndFirstNameAndLastNameAndDateOfBirth(
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