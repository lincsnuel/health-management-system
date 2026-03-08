package com.healthcore.patientservice.domain.model.vo;

import com.healthcore.patientservice.domain.model.enums.DocumentType;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Objects;

public class PatientDocument {

    private final DocumentId id;
    private final DocumentType type;
    private final String filePath;
    private final LocalDate uploadedAt;
    @Getter
    private boolean verified;

    /* ================== CONSTRUCTOR ================== */
    private PatientDocument(
            DocumentId id,
            DocumentType type,
            String filePath,
            LocalDate uploadedAt,
            boolean verified
    ) {
        this.id = Objects.requireNonNull(id, "Document ID cannot be null");
        this.type = Objects.requireNonNull(type, "Document type cannot be null");
        this.filePath = Objects.requireNonNull(filePath, "File path cannot be null");
        this.uploadedAt = uploadedAt != null ? uploadedAt : LocalDate.now();
        this.verified = verified;
    }

    /* ================== FACTORY METHODS ================== */
    public static PatientDocument of(DocumentType type, String filePath) {
        return new PatientDocument(
                DocumentId.newId(),
                type,
                filePath,
                LocalDate.now(),
                false
        );
    }

    public static PatientDocument of(DocumentType type, String filePath, LocalDate uploadedAt) {
        return new PatientDocument(
                DocumentId.newId(),
                type,
                filePath,
                uploadedAt,
                false
        );
    }

    /* ================== BUSINESS LOGIC ================== */

    /**
     * Mark the document as verified by hospital staff.
     * Only unverified documents can be verified.
     */
    public void markVerified() {
        if (verified) {
            throw new IllegalStateException("Document is already verified");
        }
        this.verified = true;
    }

    /**
     * Mark the document as unverified, for example if staff finds an issue.
     */
    public void markUnverified() {
        this.verified = false;
    }

    /**
     * Check if the document is a required type for patient registration or insurance.
     */
    public boolean isMandatory() {
        return type == DocumentType.PHOTO_ID || type == DocumentType.INSURANCE_CARD;
    }

    /* ================== GETTERS ================== */
    public DocumentId id() {
        return id;
    }

    public DocumentType type() {
        return type;
    }

    public String filePath() {
        return filePath;
    }

    public LocalDate uploadedAt() {
        return uploadedAt;
    }

}