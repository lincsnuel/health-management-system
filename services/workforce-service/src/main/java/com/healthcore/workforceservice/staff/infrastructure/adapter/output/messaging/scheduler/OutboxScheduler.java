package com.healthcore.workforceservice.staff.infrastructure.adapter.output.messaging.scheduler;

import com.healthcore.workforceservice.staff.infrastructure.adapter.output.messaging.OutboxEventProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OutboxScheduler {

    private final OutboxEventProcessor processor;

    @Scheduled(fixedDelay = 10000)
    public void run() {
        processor.process();
    }
}