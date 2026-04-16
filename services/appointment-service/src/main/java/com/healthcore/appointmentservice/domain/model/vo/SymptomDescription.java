package com.healthcore.appointmentservice.domain.model.vo;

public class SymptomDescription {

    private final String value;

    private SymptomDescription(String value) {
        if (value != null && value.length() > 300) {
            throw new IllegalArgumentException("Symptom description too long");
        }
        this.value = value;
    }

    public static SymptomDescription of(String value) {
        return new SymptomDescription(value);
    }

    public String getValue() {
        return value;
    }
}