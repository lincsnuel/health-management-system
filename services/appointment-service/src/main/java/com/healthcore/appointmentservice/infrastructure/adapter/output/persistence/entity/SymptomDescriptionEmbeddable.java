package com.healthcore.appointmentservice.infrastructure.adapter.output.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SymptomDescriptionEmbeddable {

    @Column(name = "symptom_description", length = 300)
    private String value;
}