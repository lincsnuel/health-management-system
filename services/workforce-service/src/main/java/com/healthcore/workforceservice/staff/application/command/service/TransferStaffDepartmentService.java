package com.healthcore.workforceservice.staff.application.command.service;

import com.healthcore.workforceservice.shared.domain.vo.DepartmentId;
import com.healthcore.workforceservice.shared.event.DomainEventPublisher;
import com.healthcore.workforceservice.staff.domain.model.staff.Employment;
import com.healthcore.workforceservice.staff.domain.model.staff.Staff;
import com.healthcore.workforceservice.staff.domain.repository.EmploymentRepository;
import com.healthcore.workforceservice.staff.domain.repository.StaffCommandRepository;
import com.healthcore.workforceservice.staff.domain.service.StaffTransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class TransferStaffDepartmentService {

    private final StaffCommandRepository staffRepository;
    private final EmploymentRepository employmentRepository;
    private final StaffTransferService transferService;
    private final DomainEventPublisher eventPublisher;

    public void transfer(UUID staffId, UUID tenantId, String newDepartmentId) {

        Staff staff = staffRepository.findById(staffId, tenantId)
                .orElseThrow(() -> new IllegalStateException("Staff not found"));

        Employment employment = employmentRepository.findByStaffId(staff.getStaffId())
                .orElseThrow(() -> new IllegalStateException("Employment not found"));

        DepartmentId dept = DepartmentId.of(newDepartmentId);

        // DOMAIN SERVICE ORCHESTRATION
        transferService.transfer(staff, employment, dept);

        // PERSIST BOTH
        staffRepository.save(staff);
        employmentRepository.save(employment);

        // EVENTS
        eventPublisher.publish(staff.getEvents());
        staff.clearEvents();
    }
}