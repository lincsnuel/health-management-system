package com.healthcore.tenantservice.domain.model.tenant;

import com.healthcore.tenantservice.domain.exception.*;

import java.util.*;
import java.util.stream.Collectors;

class BranchRegistry {

    private final List<FacilityBranch> branches = new ArrayList<>();

    /* ================= CONFIG ================= */

    // Can later be driven by subscription plan
    private int maxBranches = 1;


    /* ================= CORE OPERATIONS ================= */

    void add(FacilityBranch branch) {

        Objects.requireNonNull(branch, "Branch cannot be null");

        if (branches.size() >= maxBranches) {
            throw new BranchLimitExceededException(
                    "Maximum allowed branches: " + maxBranches
            );
        }

        validateUniqueName(branch);

        if (branch.isMain()) {

            // unset existing main
            branches.replaceAll(FacilityBranch::unsetMain);

        } else if (branches.isEmpty()) {

            // first branch must be main
            branch = branch.setMain();
        }

        branches.add(branch);
    }


    void remove(FacilityBranch branch) {

        if (!branches.contains(branch)) {
            throw new BranchNotFoundException("Branch not found");
        }

        if (branches.size() == 1) {
            throw new CannotRemoveLastBranchException(
                    "Tenant must have at least one branch"
            );
        }

        if (branch.isMain()) {
            throw new CannotRemoveMainBranchException(
                    "Assign another main branch before removing this one"
            );
        }

        branches.remove(branch);
    }


    void setMain(FacilityBranch branch) {

        if (!branches.contains(branch)) {
            throw new BranchNotFoundException("Branch not found");
        }

        branches.replaceAll(FacilityBranch::unsetMain);

        int index = branches.indexOf(branch.unsetMain());
        branches.set(index, branch.setMain());
    }


    /* ================= QUERIES ================= */

    Optional<FacilityBranch> getMain() {
        return branches.stream()
                .filter(FacilityBranch::isMain)
                .findFirst();
    }

    List<FacilityBranch> all() {
        return Collections.unmodifiableList(branches);
    }

    boolean isEmpty() {
        return branches.isEmpty();
    }


    /* ================= INTERNAL RULES ================= */

    private void validateUniqueName(FacilityBranch branch) {

        boolean exists = branches.stream()
                .anyMatch(b -> b.getBranchName()
                        .equalsIgnoreCase(branch.getBranchName()));

        if (exists) {
            throw new DuplicateBranchException(
                    "Branch with name '" + branch.getBranchName() + "' already exists"
            );
        }
    }


    /* ================= SUBSCRIPTION HOOK ================= */

    void updateMaxBranches(int maxBranches) {

        if (maxBranches < branches.size()) {
            throw new InvalidSubscriptionChangeException(
                    "New plan cannot support current number of branches"
            );
        }

        this.maxBranches = maxBranches;
    }
}