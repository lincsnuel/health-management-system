package com.healthcore.tenantservice.infrastructure.adapter.output.persistence.entity;

import com.healthcore.tenantservice.domain.model.enums.TenantStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(
        name = "tenant",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_tenant_subdomain", columnNames = "subdomain"),
                @UniqueConstraint(name = "uk_tenant_key", columnNames = "tenant_key")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TenantEntity {

    /* ================= IDENTIFIERS ================= */
    @Id
    @EqualsAndHashCode.Include
    @Column(name = "id", nullable = false, updatable = false)
    private UUID tenantId;

    @Column(name = "tenant_key", nullable = false, unique = true, updatable = false)
    private UUID tenantKey;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "subdomain", nullable = false, unique = true, updatable = false, length = 63)
    private String subdomain;

    /* ================= STATUS ================= */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TenantStatus status;

    @Column(name = "setup_completed", nullable = false)
    private boolean setupCompleted;

    /* ================= CORE STRUCTURE ================= */
    @Embedded
    private FacilityProfileEntity facilityProfile;

    @OneToMany(mappedBy = "tenant", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<FacilityBranchEntity> branches = new HashSet<>();

    @OneToMany(mappedBy = "tenant", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<DepartmentEntity> departments = new HashSet<>();

    /* ================= SETTINGS ================= */
    @Embedded
    private BrandingSettingsEntity brandingSettings;

    @Embedded
    private LocalizationSettingsEntity localizationSettings;

    @Embedded
    private OperationalSettingsEntity operationalSettings;

    @Embedded
    private NotificationSettingsEntity notificationSettings;

    @Embedded
    private DataRetentionPolicyEntity dataRetentionPolicy;

    /* ================= SUBSCRIPTION ================= */
    @Column(name = "subscription_plan_id", nullable = false, updatable = false)
    private UUID subscriptionPlanId;

    /* =========================================================
       =============== AGGREGATE RELATIONSHIP API ===============
       ========================================================= */

    /* ---------- BRANCHES ---------- */

    public void addBranch(FacilityBranchEntity branch) {
        if (branch == null) return;

        // prevent duplicates (based on equals/hashCode or ID)
        if (branches.contains(branch)) return;

        branch.assignTenant(this);
        branches.add(branch);
    }

    public void addBranches(Set<FacilityBranchEntity> newBranches) {
        if (newBranches == null) return;
        newBranches.forEach(this::addBranch);
    }

    public void removeBranch(FacilityBranchEntity branch) {
        if (branch == null) return;

        // orphanRemoval will handle delete
        branches.remove(branch);
    }

    /**
     * Safe bulk update (IMPORTANT for updates)
     */
    public void replaceBranches(Set<FacilityBranchEntity> newBranches) {
        branches.clear();
        if (newBranches != null) {
            newBranches.forEach(this::addBranch);
        }
    }

    /* ---------- DEPARTMENTS ---------- */

    public void addDepartment(DepartmentEntity department) {
        if (department == null) return;

        if (departments.contains(department)) return;

        department.assignTenant(this);
        departments.add(department);
    }

    public void addDepartments(Set<DepartmentEntity> newDepartments) {
        if (newDepartments == null) return;
        newDepartments.forEach(this::addDepartment);
    }

    public void removeDepartment(DepartmentEntity department) {
        if (department == null) return;

        departments.remove(department);
    }

    public void replaceDepartments(Set<DepartmentEntity> newDepartments) {
        departments.clear();
        if (newDepartments != null) {
            newDepartments.forEach(this::addDepartment);
        }
    }

    /* =========================================================
       =================== STATE MUTATORS =======================
       ========================================================= */

    public void updateName(String name) {
        this.name = name;
    }

    public void updateStatus(TenantStatus status) {
        this.status = status;
    }

    public void markSetupCompleted() {
        this.setupCompleted = true;
    }

    /* =========================================================
       =================== SETTINGS MUTATORS ====================
       ========================================================= */

    public void updateFacilityProfile(FacilityProfileEntity profile) {
        this.facilityProfile = profile;
    }

    public void updateBrandingSettings(BrandingSettingsEntity settings) {
        this.brandingSettings = settings;
    }

    public void updateLocalizationSettings(LocalizationSettingsEntity settings) {
        this.localizationSettings = settings;
    }

    public void updateOperationalSettings(OperationalSettingsEntity settings) {
        this.operationalSettings = settings;
    }

    public void updateNotificationSettings(NotificationSettingsEntity settings) {
        this.notificationSettings = settings;
    }

    public void updateDataRetentionPolicy(DataRetentionPolicyEntity policy) {
        this.dataRetentionPolicy = policy;
    }
}