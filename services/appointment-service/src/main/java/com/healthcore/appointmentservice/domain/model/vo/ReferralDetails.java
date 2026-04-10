package com.healthcore.appointmentservice.domain.model.vo;

public class ReferralDetails {

    private final String referringHospital;
    private final String notes;

    public ReferralDetails(String referringHospital, String notes) {
        this.referringHospital = referringHospital;
        this.notes = notes;
    }
}