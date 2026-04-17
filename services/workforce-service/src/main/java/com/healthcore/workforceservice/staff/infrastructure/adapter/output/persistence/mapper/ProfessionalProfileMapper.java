package com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence.mapper;

import com.healthcore.workforceservice.shared.domain.vo.StaffId;
import com.healthcore.workforceservice.staff.domain.model.staff.ProfessionalProfile;
import com.healthcore.workforceservice.staff.domain.model.vo.ProfessionalProfileId;
import com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence.entity.ProfessionalProfileEntity;
import org.springframework.stereotype.Component;


@Component
public class ProfessionalProfileMapper {

    public ProfessionalProfile toDomain(ProfessionalProfileEntity entity) {
        return ProfessionalProfile.create(
                new ProfessionalProfileId(entity.getId()),
                new StaffId(entity.getStaffId()),
                entity.getPrimarySpecialization(),
                entity.getAcademicTitle(),
                entity.isConsultant()
        );
    }

    public ProfessionalProfileEntity toEntity(ProfessionalProfile domain) {
        return ProfessionalProfileEntity.builder()
                .id(domain.getId().value())
                .staffId(domain.getStaffId().value())
                .primarySpecialization(domain.getPrimarySpecialization())
                .academicTitle(domain.getAcademicTitle())
                .consultant(domain.isConsultant())
                .build();
    }
}