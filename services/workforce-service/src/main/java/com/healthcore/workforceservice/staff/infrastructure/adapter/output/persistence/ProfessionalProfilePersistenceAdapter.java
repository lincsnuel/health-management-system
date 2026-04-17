package com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence;

import com.healthcore.workforceservice.shared.domain.vo.StaffId;
import com.healthcore.workforceservice.staff.domain.model.staff.ProfessionalProfile;
import com.healthcore.workforceservice.staff.domain.model.vo.ProfessionalProfileId;
import com.healthcore.workforceservice.staff.domain.repository.ProfessionalProfileRepository;
import com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence.entity.ProfessionalProfileEntity;
import com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence.mapper.ProfessionalProfileMapper;
import com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence.repository.ProfessionalProfileJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProfessionalProfilePersistenceAdapter implements ProfessionalProfileRepository {

    private final ProfessionalProfileJpaRepository jpaRepository;
    private final ProfessionalProfileMapper mapper;

    @Override
    public Optional<ProfessionalProfile> findById(ProfessionalProfileId id) {
        return jpaRepository.findById(id.value())
                .map(mapper::toDomain);
    }

    @Override
    public Optional<ProfessionalProfile> findByStaffId(StaffId staffId) {
        return jpaRepository.findByStaffId(staffId.value())
                .map(mapper::toDomain);
    }

    @Override
    public void save(ProfessionalProfile profile) {
        ProfessionalProfileEntity entity = mapper.toEntity(profile);
        jpaRepository.save(entity);
    }
}