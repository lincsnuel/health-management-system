package com.healthcore.appointmentservice.application.command.model;

import java.util.UUID;

public class MarkArrivalCommand {
    public UUID appointmentId;
    public UUID tenantId;
}