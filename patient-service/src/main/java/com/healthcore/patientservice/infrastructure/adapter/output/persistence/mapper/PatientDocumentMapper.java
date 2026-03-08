package com.healthcore.patientservice.infrastructure.adapter.output.persistence.mapper;

import com.healthcore.patientservice.domain.model.vo.PatientDocument;
import com.healthcore.patientservice.infrastructure.adapter.output.persistence.entity.PatientDocumentEntity;

import java.time.LocalDate;

public class PatientDocumentMapper {

    public static PatientDocument toDomain(PatientDocumentEntity entity) {
        if (entity == null) return null;

        return PatientDocument.of(
                entity.getType(),
                entity.getFilePath(),
                entity.getUploadedAt()
        );
    }

    public static PatientDocumentEntity toEntity(PatientDocument domain) {
        if (domain == null) return null;

        return PatientDocumentEntity.builder()
                .type(domain.type())
                .filePath(domain.filePath())
                .uploadedAt(domain.uploadedAt() != null ? domain.uploadedAt() : LocalDate.now())
                .build();
    }
}