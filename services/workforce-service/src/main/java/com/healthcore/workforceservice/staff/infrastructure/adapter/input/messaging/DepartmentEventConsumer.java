package com.healthcore.workforceservice.staff.infrastructure.adapter.input.messaging;

import com.healthcore.workforceservice.shared.event.DepartmentCreatedEvent;
import com.healthcore.workforceservice.shared.event.DepartmentUpdatedEvent;
import com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence.repository.DepartmentProjectionRepository;
import com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence.entity.DepartmentProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DepartmentEventConsumer {

    private final DepartmentProjectionRepository repository;

    public void handleDepartmentCreated(DepartmentCreatedEvent event) {

        repository.save(
                DepartmentProjection.builder()
                .departmentId(event.getDepartmentId())
                .tenantId(event.getTenantId())
                .name(event.getName())
                .active(true).build()
        );
    }

    public void handleDepartmentUpdated(DepartmentUpdatedEvent event) {

        DepartmentProjection.DepartmentProjectionBuilder projectionBuilder =
                repository.findById(event.getDepartmentId())
                        .map(DepartmentProjection::toBuilder)
                        .orElseThrow(() -> new RuntimeException("Department not found"));

        projectionBuilder.name(event.getName());
        projectionBuilder.active(event.isActive());

        repository.save(projectionBuilder.build());
    }
}