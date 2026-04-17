package com.healthcore.workforceservice.shared.outbox.scheduler;

import com.healthcore.workforceservice.shared.outbox.messaging.OutboxProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

@RequiredArgsConstructor
public class OutboxScheduler {

    private final OutboxProcessor processor;

    @Scheduled(fixedDelay = 5000)
    public void run() {
        processor.process();
    }
}