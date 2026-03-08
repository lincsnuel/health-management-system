package com.healthcore.patientservice.infrastructure.adapter.output.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class NextOfKinEntity {

    @Column(name = "nok_full_name", nullable = false)
    private String fullName;

    @Column(name = "nok_relationship", nullable = false)
    private String relationship;

    @Column(name = "nok_contact_number", nullable = false, length = 15)
    private String contactNumber;

    @Column(name = "nok_address")
    private String address;
}
