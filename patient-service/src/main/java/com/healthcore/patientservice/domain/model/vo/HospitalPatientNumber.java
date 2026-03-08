package com.healthcore.patientservice.domain.model.vo;

public record HospitalPatientNumber(String value) {

    public HospitalPatientNumber {
        if (value != null) {
            value = value.trim().toUpperCase(); // normalize
        }
    }

    public static HospitalPatientNumber of(String number) {
        return new HospitalPatientNumber(number);
    }
}