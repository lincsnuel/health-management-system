package com.healthcore.workforceservice.infrastructure.adapter.output.persistence.entity;

import com.healthcore.workforceservice.domain.model.enums.IdentityType;
import com.healthcore.workforceservice.domain.model.vo.NationalIdentity;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * JPA Embeddable for the NationalIdentity Value Object.
 * Maps IdentityType (NIN, BVN, etc.) and the unique identification number.
 */
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class NationalIdentityEmbeddable {

    @Enumerated(EnumType.STRING)
    @Column(name = "identity_type", nullable = false)
    private IdentityType type;

    @Column(name = "identity_number", nullable = false)
    private String number;

    /**
     * Bridge from Domain to Persistence
     */
    public static NationalIdentityEmbeddable fromDomain(NationalIdentity identity) {
        if (identity == null) return null;
        return new NationalIdentityEmbeddable(identity.type(), identity.number());
    }

    /**
     * Bridge from Persistence to Domain
     */
    public NationalIdentity toDomain() {
        return new NationalIdentity(type, number);
    }
}