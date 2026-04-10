package com.healthcore.appointmentservice.infrastructure.adapter.output.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReferralDetailsEmbeddable {

    @Column(name = "referring_hospital")
    private String referringHospital;

    @Column(name = "referral_notes")
    private String notes;
}