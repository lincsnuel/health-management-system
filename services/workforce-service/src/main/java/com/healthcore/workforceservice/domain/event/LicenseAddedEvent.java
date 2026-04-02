package com.healthcore.workforceservice.domain.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = false)
public class LicenseAddedEvent extends DomainEvent {

    private final UUID staffId;
    private final String licenseNo;
    private final String body;


    @Override
    public String getAggregateId() {
        return staffId.toString();
    }
}
