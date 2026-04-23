package com.healthcore.workforceservice.shared.messaging.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "department.exchange";

    public static final String CREATED_QUEUE = "department.created.queue";
    public static final String UPDATED_QUEUE = "department.updated.queue";

    public static final String CREATED_ROUTING_KEY = "department.created";
    public static final String UPDATED_ROUTING_KEY = "department.updated";

    @Bean
    public TopicExchange departmentExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue createdQueue() {
        return new Queue(CREATED_QUEUE);
    }

    @Bean
    public Queue updatedQueue() {
        return new Queue(UPDATED_QUEUE);
    }

    @Bean
    public Binding createdBinding() {
        return BindingBuilder.bind(createdQueue())
                .to(departmentExchange())
                .with(CREATED_ROUTING_KEY);
    }

    @Bean
    public Binding updatedBinding() {
        return BindingBuilder.bind(updatedQueue())
                .to(departmentExchange())
                .with(UPDATED_ROUTING_KEY);
    }
}