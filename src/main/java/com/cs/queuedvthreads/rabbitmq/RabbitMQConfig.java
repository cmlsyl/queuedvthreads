package com.cs.queuedvthreads.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RabbitMQConfig {

    @Value("${cs.rabbitmq.queue.main.name}")
    private String mainQueueName;

    @Value("${cs.rabbitmq.queue.retry.name}")
    private String retryQueueName;

    @Value("${cs.rabbitmq.queue.unprocessed.name}")
    private String unprocessedQueueName;

    @Value("${cs.rabbitmq.exchange}")
    private String exchange;

    @Value("${cs.rabbitmq.routing-key}")
    private String routingKey;

    @Value("${cs.rabbitmq.queue.retry.delay-in-ms}")
    private Integer retryDelay;

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(exchange);
    }

    @Bean
    Queue mainQueue() {
        return QueueBuilder.durable(mainQueueName)
                .deadLetterExchange(exchange)
                .deadLetterRoutingKey(retryQueueName)
                .quorum()
                .build();
    }

    @Bean
    Queue retryQueue() {
        return QueueBuilder.durable(retryQueueName)
                .deadLetterExchange(exchange)
                .deadLetterRoutingKey(routingKey)
                .ttl(retryDelay)
                .quorum()
                .build();
    }

    @Bean
    Queue unprocessedQueue() {
        return QueueBuilder.durable(unprocessedQueueName)
                .quorum()
                .build();
    }

    @Bean
    Binding mainBinding(Queue mainQueue, TopicExchange exchange) {
        return BindingBuilder.bind(mainQueue).to(exchange).with(routingKey);
    }

    @Bean
    Binding retryBinding(Queue retryQueue, TopicExchange exchange) {
        return BindingBuilder.bind(retryQueue).to(exchange).with(retryQueueName);
    }

    @Bean
    Binding unprocessedBinding(Queue unprocessedQueue, TopicExchange exchange) {
        return BindingBuilder.bind(unprocessedQueue).to(exchange).with(unprocessedQueueName);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}