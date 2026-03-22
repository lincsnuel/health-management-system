package com.healthcore.tenantservice.infrastructure.adapter.output.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "facility_branches")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class FacilityBranchEntity {

    @Id
    @Column(name = "branch_id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "branch_name", nullable = false)
    private String branchName;

    @Embedded
    private AddressEntity address;

    @Embedded
    private ContactInfoEntity contactInfo;

    @Column(name = "is_main", nullable = false)
    private boolean main;

    @Column(name = "is_active", nullable = false)
    private boolean active;

    /* ================= PARENT REFERENCE ================= */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private TenantEntity tenant;

    /* ================= HELPERS ================= */
    public void assignTenant(TenantEntity tenant) {
        this.tenant = tenant;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    public void setMain() {
        this.main = true;
    }

    public void unsetMain() {
        this.main = false;
    }

    public void updateBranchName(String name) {
        this.branchName = name;
    }

    public void updateAddress(AddressEntity newAddress) {
        this.address = newAddress;
    }

    public void updateContactInfo(ContactInfoEntity newContact) {
        this.contactInfo = newContact;
    }
}