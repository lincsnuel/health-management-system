package com.healthcore.workforceservice.application.command.service;

import com.healthcore.workforceservice.application.command.event.DomainEventPublisher;
import com.healthcore.workforceservice.application.command.usecase.ActivateStaffUseCase;
import com.healthcore.workforceservice.domain.model.staff.Staff;
import com.healthcore.workforceservice.domain.repository.StaffCommandRepository;
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

    @Override
    public void activate(UUID staffId, UUID tenantId) {

        Staff staff = staffRepository.findById(staffId, tenantId)
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        staff.activate();

        staffRepository.save(staff);

        eventPublisher.publish(staff.getDomainEvents());
        staff.clearDomainEvents();
    }
}