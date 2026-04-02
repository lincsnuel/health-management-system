package com.healthcore.workforceservice.application.command.validator;

import com.healthcore.workforceservice.application.command.model.RegisterStaffCommand;
import com.healthcore.workforceservice.application.exception.DuplicateStaffEmailException;
import com.healthcore.workforceservice.domain.repository.StaffCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailUniquenessValidator implements CommandValidator<RegisterStaffCommand> {

    private final StaffCommandRepository staffRepository;

    @Override
    public void validate(RegisterStaffCommand command) {

        if (command.email() == null) {
            return;
        }

        boolean exists = staffRepository.existsByEmail(
                command.tenantId(),
                command.email()
        );

        if (exists) {
            throw new DuplicateStaffEmailException(command.email());
        }
    }
}