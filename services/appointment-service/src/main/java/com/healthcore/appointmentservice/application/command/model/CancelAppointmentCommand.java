package com.healthcore.appointmentservice.application.command.model;

import java.util.UUID;

public class CancelAppointmentCommand {
    public UUID appointmentId;
    public UUID tenantId;
}