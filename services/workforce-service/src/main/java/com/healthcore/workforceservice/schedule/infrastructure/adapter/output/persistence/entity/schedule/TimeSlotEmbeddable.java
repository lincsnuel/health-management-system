package com.healthcore.workforceservice.schedule.infrastructure.adapter.output.persistence.entity.schedule;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Embeddable
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlotEmbeddable {

    private LocalTime start;
    private LocalTime end;
}