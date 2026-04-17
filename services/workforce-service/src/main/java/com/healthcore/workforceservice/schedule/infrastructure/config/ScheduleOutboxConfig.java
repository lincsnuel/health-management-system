package com.healthcore.workforceservice.schedule.infrastructure.config;

import com.healthcore.workforceservice.schedule.infrastructure.adapter.output.messaging.ScheduleTopicResolver;
import com.healthcore.workforceservice.shared.outbox.messaging.OutboxProcessor;
import com.healthcore.workforceservice.shared.outbox.messaging.kafka.KafkaEventPublisher;
import com.healthcore.workforceservice.shared.outbox.persistence.repository.OutboxRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class ScheduleOutboxConfig {

    @Bean
    public OutboxProcessor scheduleOutboxProcessor(
            OutboxRepository repository,
            KafkaTemplate<String, String> kafkaTemplate,
            ScheduleTopicResolver topicResolver
    ) {
        return new OutboxProcessor(
                repository,
                new KafkaEventPublisher(kafkaTemplate, topicResolver),
                "SCHEDULE"
        );
    }
}