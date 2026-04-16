package com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence;

import com.healthcore.workforceservice.staff.domain.model.staff.Staff;
import com.healthcore.workforceservice.staff.domain.repository.StaffCommandRepository;
import com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence.mapper.StaffPersistenceMapper;
import com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence.repository.StaffJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class StaffCommandPersistenceAdapter implements StaffCommandRepository {

    private final StaffJpaRepository repository;

    @Override
    public Staff save(Staff staff) {
        return StaffPersistenceMapper.toDomain(
                repository.save(StaffPersistenceMapper.toEntity(staff))
        );
    }

    @Override
    public Optional<Staff> findById(UUID staffId, UUID tenantId) {
        return Optional.empty();
    }

    @Override
    public Optional<Staff> findByEmail(String email) {
        return Optional.ofNullable(StaffPersistenceMapper
                .toDomain(repository.findByEmail(email)
                        .orElseThrow()));
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public boolean existsByEmployeeId(String employeeId) {
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
                dateOfBirth);
    }
}