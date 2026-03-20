package com.healthcore.tenantservice.infrastructure.adapter.output.persistence.entity;

import com.healthcore.tenantservice.domain.model.enums.TenantStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tenants")
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
    private UUID id;

    @Column(name = "tenant_key", nullable = false, unique = true, updatable = false)
    private UUID tenantKey;

    @Column(name = "name", nullable = false)
    private String name;

    /* ================= STATUS ================= */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TenantStatus status;

    @Column(name = "setup_completed", nullable = false)
    private boolean setupCompleted;

    /* ================= CORE STRUCTURE ================= */
    @Embedded
    private FacilityProfileEntity facilityProfile;

    @OneToMany(mappedBy = "tenant", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<FacilityBranchEntity> branches = new HashSet<>();

    @OneToMany(mappedBy = "tenant", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
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

    /* ================= COLLECTION HELPERS ================= */
    public void addBranch(FacilityBranchEntity branch) {
        branch.setTenant(this);
        branches.add(branch);
    }

    public void removeBranch(FacilityBranchEntity branch) {
        branches.remove(branch);
        branch.setTenant(null);
    }

    public void addDepartment(DepartmentEntity department) {
        department.setTenant(this);
        departments.add(department);
    }

    public void removeDepartment(DepartmentEntity department) {
        departments.remove(department);
        department.setTenant(null);
    }

//    public void setFacilityProfile(FacilityProfileEntity profile) {
//        this.facilityProfile = profile;
//    }
//
//    public void setBrandingSettings(BrandingSettingsEntity settings) {
//        this.brandingSettings = settings;
//    }
//
//    public void setLocalizationSettings(LocalizationSettingsEntity settings) {
//        this.localizationSettings = settings;
//    }
//
//    public void setOperationalSettings(OperationalSettingsEntity settings) {
//        this.operationalSettings = settings;
//    }
//
//    public void setNotificationSettings(NotificationSettingsEntity settings) {
//        this.notificationSettings = settings;
//    }
//
//    public void setDataRetentionPolicy(DataRetentionPolicyEntity policy) {
//        this.dataRetentionPolicy = policy;
//    }
//
//    public void setStatus(TenantStatus status) {
//        this.status = status;
//    }
//
//    public void setSetupCompleted(boolean setupCompleted) {
//        this.setupCompleted = setupCompleted;
//    }
}