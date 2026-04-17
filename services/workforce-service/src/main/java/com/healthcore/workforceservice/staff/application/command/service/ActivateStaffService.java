package com.healthcore.workforceservice.staff.application.command.service;

import com.healthcore.workforceservice.shared.event.DomainEventPublisher;
import com.healthcore.workforceservice.staff.application.command.usecase.ActivateStaffUseCase;
import com.healthcore.workforceservice.staff.domain.model.staff.Credentialing;
import com.healthcore.workforceservice.staff.domain.model.staff.Staff;
import com.healthcore.workforceservice.staff.domain.repository.CredentialingRepository;
import com.healthcore.workforceservice.staff.domain.repository.StaffCommandRepository;
import com.healthcore.workforceservice.staff.domain.service.CredentialValidationService;
import com.healthcore.workforceservice.staff.domain.service.StaffActivationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ActivateStaffService implements ActivateStaffUseCase {

    private final DomainEventPublisher eventPublisher;

    private final StaffCommandRepository staffRepository;
    private final CredentialingRepository credentialingRepository;

    private final StaffActivationService activationService;
    private final CredentialValidationService credentialValidationService;

    @Override
    public void activate(UUID staffId) {

        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new IllegalStateException("Staff not found"));

        Credentialing credentialing =
                credentialingRepository.findByStaffId(staff.getStaffId())
                        .orElseThrow(() -> new IllegalStateException("Credentialing not found"));

        // 1. ENFORCE CROSS-AGGREGATE RULES (DOMAIN SERVICE)
        credentialValidationService.enforceEligibility(staff, credentialing);

        // 2. ACTIVATE (DOMAIN SERVICE OR AGGREGATE WRAPPER)
        activationService.activateStaff(staff, credentialing);

        // 3. PERSIST STATE CHANGE
        staffRepository.save(staff);

        // 4. EVENTS
        eventPublisher.publish(staff.getEvents());
        staff.clearDomainEvents();
    }
}