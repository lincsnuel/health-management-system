package com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence;

import com.healthcore.workforceservice.shared.outbox.service.OutboxService;
import com.healthcore.workforceservice.staff.domain.model.staff.Staff;
import com.healthcore.workforceservice.staff.domain.repository.StaffCommandRepository;
import com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence.mapper.StaffPersistenceMapper;
import com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence.repository.StaffJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class StaffCommandPersistenceAdapter implements StaffCommandRepository {

    private final StaffJpaRepository repository;
    private final StaffPersistenceMapper mapper;
    private final OutboxService outboxService;

    @Override
    @Transactional
    public Staff save(Staff staff) {

        // 1. Map domain → entity
        var entity = mapper.toEntity(staff);

        // 2. Persist aggregate
        var savedEntity = repository.save(entity);

        // 3. Persist domain events → outbox
        outboxService.saveEvents(staff.getEvents());

        // 4. Clear domain events (AFTER successful persistence)
        staff.clearDomainEvents();

        // 5. Return fresh domain (mapped from DB state)
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Staff> findById(UUID staffId) {
        return repository.findById(staffId)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Staff> findByEmail(String email) {
        return repository.findByEmail(email)
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public boolean existsByEmployeeId(String employeeId) {
        // ⚠️ Staff doesn't own employeeId — Employment aggregate does
        // This should ideally be handled via EmploymentRepository
        return false;
    }

    @Override
    public boolean existsByNameAndDateOfBirth(
            String firstName,
            String lastName,
            LocalDate dateOfBirth) {

        return repository.existsByFullName_FirstNameAndFullName_LastNameAndDateOfBirth(
                firstName,
                lastName,
                dateOfBirth
        );
    }
}