package com.healthcore.workforceservice.application.command.service;

import com.healthcore.workforceservice.application.command.event.DomainEventPublisher;
import com.healthcore.workforceservice.application.command.model.RegisterStaffCommand;
import com.healthcore.workforceservice.application.command.usecase.RegisterStaffUseCase;
import com.healthcore.workforceservice.application.command.validator.CommandValidator;
import com.healthcore.workforceservice.domain.model.staff.Staff;
import com.healthcore.workforceservice.domain.model.vo.EmailAddress;
import com.healthcore.workforceservice.domain.model.vo.FullName;
import com.healthcore.workforceservice.domain.model.vo.NationalIdentity;
import com.healthcore.workforceservice.domain.model.vo.PhoneNumber;
import com.healthcore.workforceservice.domain.repository.StaffCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RegisterStaffService implements RegisterStaffUseCase {

    private final DomainEventPublisher eventPublisher;
    private final StaffCommandRepository staffRepository;
    private final List<CommandValidator<RegisterStaffCommand>> validators;

    @Override
    public String registerStaff(RegisterStaffCommand command) {

        validators.forEach(v -> v.validate(command));

        // 2. Create aggregate
        Staff staff = Staff.create(
                command.tenantId(),
                new FullName(command.firstName(), command.lastName(), command.middleName()),
                new EmailAddress(command.email()),
                new PhoneNumber(command.phoneNumber()),
                command.gender(),
                command.dateOfBirth(),
                command.staffType(),
                new NationalIdentity(command.identityType(), command.identityNumber())
        );

        // 3. Persist
        staffRepository.save(staff);

        // 4. Publish events
        eventPublisher.publish(staff.getDomainEvents());
        staff.clearDomainEvents();

        return staff.getStaffId().value().toString();
    }
}