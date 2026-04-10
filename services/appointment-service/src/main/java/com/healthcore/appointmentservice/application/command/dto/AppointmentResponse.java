package com.healthcore.appointmentservice.application.command.dto;

import java.util.UUID;

public class AppointmentResponse {

    public UUID appointmentId;
    public String status;
    public UUID billingId;

    public static AppointmentResponse from(UUID id, String status) {
        AppointmentResponse res = new AppointmentResponse();
        res.appointmentId = id;
        res.status = status;
        return res;
    }

    public static AppointmentResponse from(UUID id, String status, UUID billingId) {
        AppointmentResponse res = new AppointmentResponse();
        res.appointmentId = id;
        res.status = status;
        res.billingId = billingId;
        return res;
    }
}