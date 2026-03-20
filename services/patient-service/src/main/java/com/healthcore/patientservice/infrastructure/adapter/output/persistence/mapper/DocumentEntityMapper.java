package com.healthcore.patientservice.infrastructure.adapter.output.persistence.mapper;

import com.healthcore.patientservice.domain.model.patient.PatientDocument;
import com.healthcore.patientservice.domain.model.vo.*;
import com.healthcore.patientservice.infrastructure.adapter.output.persistence.entity.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class DocumentEntityMapper {

    /* ================= DOMAIN -> ENTITY ================= */
    public PatientDocumentEntity toEntity(PatientDocument domain) {
        if (domain == null) return null;

        return PatientDocumentEntity.builder()
                .id(domain.getId() != null ? domain.getId().value() : null)
                .type(domain.getType())
                .filePath(domain.getFilePath())
                .uploadedAt(domain.getUploadedAt())
                .verified(domain.isVerified())
                .build();
    }

    /* ================= ENTITY -> DOMAIN ================= */
    public PatientDocument toDomain(PatientDocumentEntity entity) {
        if (entity == null) return null;

        DocumentId id = entity.getId() != null ? DocumentId.of(entity.getId()) : null;

        // Use the static factory methods since constructor is private
        PatientDocument document = PatientDocument.reconstruct(
                DocumentId.of(entity.getId()),
                entity.getType(),
                entity.getFilePath(),
                entity.getUploadedAt(),
                entity.isVerified()
        );

        if (entity.isVerified()) {
            document.markVerified();
        }

        return document;
    }

    /* ================= ID CONVERSION ================= */
    public UUID map(DocumentId id) {
        return id == null ? null : id.value();
    }

    public DocumentId map(UUID id) {
        return id == null ? null : DocumentId.of(id);
    }

    /* ================= COLLECTION MAPPERS ================= */
    public List<PatientDocument> toDomainList(Set<PatientDocumentEntity> entities) {
        if (entities == null) return List.of();
        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    public Set<PatientDocumentEntity> toEntitySet(List<PatientDocument> documents) {
        if (documents == null) return Set.of();
        return documents.stream()
                .map(this::toEntity)
                .collect(Collectors.toSet());
    }

    /* ================= BACK REFERENCE ================= */
    public void linkPatient(PatientDocumentEntity entity, PatientEntity patient) {
        if (entity != null && patient != null) {
            entity.assignPatient(patient);
        }
    }
}