package com.healthcore.workforceservice.staff.infrastructure.config;

import com.healthcore.workforceservice.shared.outbox.messaging.OutboxProcessor;
import com.healthcore.workforceservice.shared.outbox.messaging.kafka.KafkaEventPublisher;
import com.healthcore.workforceservice.shared.outbox.persistence.repository.OutboxRepository;
import com.healthcore.workforceservice.staff.infrastructure.adapter.output.messaging.StaffTopicResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class StaffOutboxConfig {

    @Bean
    public OutboxProcessor staffOutboxProcessor(
            OutboxRepository repository,
            KafkaTemplate<String, String> kafkaTemplate,
            StaffTopicResolver topicResolver
    ) {
        return new OutboxProcessor(
                repository,
                new KafkaEventPublisher(kafkaTemplate, topicResolver),
                "STAFF"
        );
    }
}