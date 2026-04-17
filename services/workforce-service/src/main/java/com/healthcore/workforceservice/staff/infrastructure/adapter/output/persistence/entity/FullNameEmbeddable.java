package com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence.entity;

import com.healthcore.workforceservice.staff.domain.model.vo.FullName;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * JPA Embeddable for the FullName Value Object.
 */
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // Required by JPA
@AllArgsConstructor
public class FullNameEmbeddable {

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "middle_name") // Nullable for those without middle names
    private String middleName;

    /**
     * Map from JPA Embeddable back to Domain VO
     */
    public FullName toDomain() {
        return new FullName(firstName, lastName, middleName);
    }
}