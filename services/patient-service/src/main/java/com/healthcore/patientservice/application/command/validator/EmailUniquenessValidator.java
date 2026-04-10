package com.healthcore.patientservice.application.command.validator;

import com.healthcore.patientservice.application.command.model.RegisterPatientCommand;
import com.healthcore.patientservice.domain.repository.PatientCommandRepository;
import com.healthcore.patientservice.application.exception.DuplicatePatientEmailException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailUniquenessValidator implements CommandValidator<RegisterPatientCommand> {

    private final PatientCommandRepository patientRepository;

    @Override
    public void validate(RegisterPatientCommand command) {

        if (command.email() == null) {
            return;
        }

        boolean exists = patientRepository.existsByEmail(command.email());

        if (exists) {
            throw new DuplicatePatientEmailException(command.email());
        }
    }
}