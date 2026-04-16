package com.healthcore.workforceservice.staff.application.command.validator;

import com.healthcore.workforceservice.staff.application.command.model.RegisterStaffCommand;
import com.healthcore.workforceservice.staff.application.exception.DepartmentNotFoundException;
import com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence.repository.DepartmentProjectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DepartmentExistsValidator implements CommandValidator<RegisterStaffCommand> {

    private final DepartmentProjectionRepository repository;

    @Override
    public void validate(RegisterStaffCommand command) {

        if (command.departmentId() == null) {
            return;
        }

        boolean exists = repository.existsByDepartmentId(
                command.departmentId()
        );

        if (!exists) {
            throw new DepartmentNotFoundException("Department does not exist");
        }
    }
}