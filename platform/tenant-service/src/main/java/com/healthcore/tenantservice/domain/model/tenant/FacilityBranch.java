package com.healthcore.tenantservice.domain.model.tenant;

import com.healthcore.tenantservice.domain.exception.InvalidBranchException;
import com.healthcore.tenantservice.domain.model.vo.Address;
import com.healthcore.tenantservice.domain.model.vo.ContactInfo;

import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

@Getter
public class FacilityBranch {

    private final UUID id;

    private final String branchName;

    private final Address address;

    private final ContactInfo contactInfo;

    private final boolean main;

    private final boolean active;


    private FacilityBranch(
            UUID id,
            String branchName,
            Address address,
            ContactInfo contactInfo,
            boolean main,
            boolean active
    ) {
        this.id = Objects.requireNonNull(id);
        this.branchName = normalize(branchName);
        this.address = Objects.requireNonNull(address);
        this.contactInfo = Objects.requireNonNull(contactInfo);
        this.main = main;
        this.active = active;
    }


    /* ================= FACTORY ================= */

    public static FacilityBranch create(
            String branchName,
            Address address,
            ContactInfo contactInfo,
            boolean main
    ) {
        return new FacilityBranch(
                UUID.randomUUID(),
                branchName,
                address,
                contactInfo,
                main,
                true
        );
    }


    public static FacilityBranch reconstruct(
            UUID id,
            String branchName,
            Address address,
            ContactInfo contactInfo,
            boolean main,
            boolean active
    ) {
        return new FacilityBranch(
                id,
                branchName,
                address,
                contactInfo,
                main,
                active
        );
    }


    /* ================= STATE TRANSITIONS ================= */

    public FacilityBranch deactivate() {
        return new FacilityBranch(
                this.id,
                this.branchName,
                this.address,
                this.contactInfo,
                this.main,
                false
        );
    }

    public FacilityBranch activate() {
        return new FacilityBranch(
                this.id,
                this.branchName,
                this.address,
                this.contactInfo,
                this.main,
                true
        );
    }

    public FacilityBranch setMain() {
        return new FacilityBranch(
                this.id,
                this.branchName,
                this.address,
                this.contactInfo,
                true,
                this.active
        );
    }

    public FacilityBranch unsetMain() {
        return new FacilityBranch(
                this.id,
                this.branchName,
                this.address,
                this.contactInfo,
                false,
                this.active
        );
    }


    /* ================= VALIDATION ================= */

    private String normalize(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidBranchException("Branch name cannot be blank");
        }
        return value.trim();
    }
}