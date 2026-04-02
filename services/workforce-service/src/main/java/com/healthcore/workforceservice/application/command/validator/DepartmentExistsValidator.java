package com.healthcore.workforceservice.application.command.validator;

import com.healthcore.workforceservice.application.command.model.RegisterStaffCommand;
import com.healthcore.workforceservice.application.exception.DepartmentNotFoundException;
import com.healthcore.workforceservice.application.port.output.DepartmentValidationPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DepartmentExistsValidator implements CommandValidator<RegisterStaffCommand> {

    private final DepartmentValidationPort departmentValidationPort;

    @Override
    public void validate(RegisterStaffCommand command) {

        if (command.departmentId() == null) {
            return;
        }

        boolean exists = departmentValidationPort.exists(
                command.tenantId(),
                command.departmentId()
        );

        if (!exists) {
            throw new DepartmentNotFoundException("Department does not exist");
        }
    }
}