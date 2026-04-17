package com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence;

import com.healthcore.workforceservice.shared.domain.vo.StaffId;
import com.healthcore.workforceservice.staff.domain.model.staff.Employment;
import com.healthcore.workforceservice.staff.domain.model.vo.EmploymentId;
import com.healthcore.workforceservice.staff.domain.repository.EmploymentRepository;
import com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence.entity.EmploymentEntity;
import com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence.mapper.EmploymentMapper;
import com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence.repository.EmploymentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EmploymentPersistenceAdapter implements EmploymentRepository {

    private final EmploymentJpaRepository jpaRepository;
    private final EmploymentMapper mapper;

    @Override
    public Optional<Employment> findById(EmploymentId id) {
        return jpaRepository.findById(id.value())
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Employment> findByStaffId(StaffId staffId) {
        return jpaRepository.findByStaffId(staffId.value())
                .map(mapper::toDomain);
    }

    @Override
    public void save(Employment employment) {
        EmploymentEntity entity = mapper.toEntity(employment);
        jpaRepository.save(entity);
    }
}