package com.healthcore.workforceservice.staff.application.command.service;

import com.healthcore.workforceservice.shared.domain.vo.StaffId;
import com.healthcore.workforceservice.shared.event.DomainEventPublisher;
import com.healthcore.workforceservice.staff.application.command.model.RegisterStaffCommand;
import com.healthcore.workforceservice.staff.application.command.usecase.RegisterStaffUseCase;
import com.healthcore.workforceservice.staff.application.command.validator.CommandValidator;
import com.healthcore.workforceservice.staff.domain.model.staff.Credentialing;
import com.healthcore.workforceservice.staff.domain.model.staff.Employment;
import com.healthcore.workforceservice.staff.domain.model.staff.ProfessionalProfile;
import com.healthcore.workforceservice.staff.domain.model.staff.Staff;
import com.healthcore.workforceservice.staff.domain.model.vo.*;
import com.healthcore.workforceservice.staff.domain.repository.CredentialingRepository;
import com.healthcore.workforceservice.staff.domain.repository.EmploymentRepository;
import com.healthcore.workforceservice.staff.domain.repository.ProfessionalProfileRepository;
import com.healthcore.workforceservice.staff.domain.repository.StaffCommandRepository;
import com.healthcore.workforceservice.staff.domain.service.StaffOnboardingResult;
import com.healthcore.workforceservice.staff.domain.service.StaffOnboardingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class RegisterStaffService implements RegisterStaffUseCase {

    private final DomainEventPublisher eventPublisher;

    private final StaffCommandRepository staffRepository;
    private final EmploymentRepository employmentRepository;
    private final ProfessionalProfileRepository profileRepository;
    private final CredentialingRepository credentialingRepository;

    private final StaffOnboardingService onboardingService;

    private final List<CommandValidator<RegisterStaffCommand>> validators;

    @Override
    public String registerStaff(RegisterStaffCommand command) {

        // 1. Validate input
        validators.forEach(v -> v.validate(command));

        // 2. Create aggregates (pure domain objects)
        Staff staff = Staff.register(
                StaffId.of(UUID.randomUUID()),
                new FullName(command.firstName(), command.lastName(), command.middleName()),
                new EmailAddress(command.email()),
                new PhoneNumber(command.phoneNumber()),
                command.gender(),
                command.dateOfBirth(),
                command.staffType()
        );

        Employment employment = Employment.create(
                EmploymentId.newId(),
                staff.getStaffId(),
                command.employeeId(),
                command.employmentType(),
                command.dateOfHire()
        );

        ProfessionalProfile profile = ProfessionalProfile.create(
                ProfessionalProfileId.newId(),
                staff.getStaffId(),
                command.specialization(),
                command.academicTitle(),
                command.isConsultant()
        );

        Credentialing credentialing = new Credentialing(
                CredentialingId.newId(),
                staff.getStaffId()
        );

        // 3. Domain orchestration (cross-aggregate rules)
        StaffOnboardingResult result = onboardingService.onboard(
                staff,
                employment,
                profile,
                credentialing
        );

        // 4. Persist ALL aggregates
        staffRepository.save(result.staff());
        employmentRepository.save(result.employment());
        profileRepository.save(result.profile());
        credentialingRepository.save(result.credentialing());

        // 5. Publish events (AFTER persistence)
        publish(staff, result);

        return staff.getStaffId().value().toString();
    }

    private void publish(Staff staff, StaffOnboardingResult result) {
        eventPublisher.publish(staff.getEvents());
        staff.clearDomainEvents();
    }
}