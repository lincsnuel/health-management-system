package com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence.entity;

import com.healthcore.workforceservice.staff.domain.model.vo.StaffRank;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * JPA Embeddable for the StaffRank Value Object.
 * Supports Nigerian public health cadres (e.g., Medical Officer) and optional grade levels.
 */
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class StaffRankEmbeddable {

    @Column(name = "rank_cadre")
    private String cadre;

    @Column(name = "rank_level") // Nullable for private facilities
    private String level;

    /**
     * Maps from Domain VO to JPA Embeddable.
     * Since StaffRank is optional in the Staff aggregate,
     * this handles the null check before mapping.
     */
    public static StaffRankEmbeddable fromDomain(StaffRank rank) {
        if (rank == null) return null;
        return new StaffRankEmbeddable(rank.cadre(), rank.level());
    }

    /**
     * Maps from JPA Embeddable back to Domain VO.
     */
    public StaffRank toDomain() {
        return new StaffRank(cadre, level);
    }
}