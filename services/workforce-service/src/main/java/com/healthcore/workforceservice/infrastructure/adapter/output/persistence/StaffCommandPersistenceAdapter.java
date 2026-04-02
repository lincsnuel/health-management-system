package com.healthcore.workforceservice.infrastructure.adapter.output.persistence;

import com.healthcore.workforceservice.domain.model.staff.Staff;
import com.healthcore.workforceservice.domain.repository.StaffCommandRepository;
import com.healthcore.workforceservice.infrastructure.adapter.output.persistence.mapper.StaffPersistenceMapper;
import com.healthcore.workforceservice.infrastructure.adapter.output.persistence.repository.StaffJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class StaffCommandPersistenceAdapter implements StaffCommandRepository {

    private final StaffJpaRepository repository;
    private final StaffPersistenceMapper staffMapper;

    @Override
    public Staff save(Staff staff) {
        return staffMapper.toDomain(
                repository.save(staffMapper.toEntity(staff))
        );
    }

    @Override
    public Optional<Staff> findById(UUID staffId, UUID tenantId) {
        return Optional.empty();
    }

    @Override
    public Optional<Staff> findByEmail(String tenantId, String email) {
        return Optional.ofNullable(staffMapper
                .toDomain(repository.findByTenantIdAndEmail(tenantId, email)
                        .orElseThrow()));
    }

    @Override
    public boolean existsByEmail(String tenantId, String email) {
        return repository.existsByTenantIdAndEmail(tenantId, email);
    }

    @Override
    public boolean existsByEmployeeId(String tenantId, String employeeId) {
        return false;
    }

    @Override
    public boolean existsByTenantIdAndNameAndDateOfBirth(
            String tenantId,
            String firstName,
            String lastName,
            LocalDate dateOfBirth) {
        return repository.existsByTenantIdAndFullName_FirstNameAndFullName_LastNameAndDateOfBirth(
                tenantId,
                firstName,
                lastName,
                dateOfBirth);
    }
}