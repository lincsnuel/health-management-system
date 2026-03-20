package com.healthcore.patientservice.application.query.model;

import com.healthcore.patientservice.domain.model.enums.DocumentType;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Query model representing a patient's document
 * Used for read operations (queries) only
 */
public record DocumentDetail(
        UUID documentId,
        DocumentType type,
        String filePath,
        LocalDate uploadedAt,
        boolean verified
) {}