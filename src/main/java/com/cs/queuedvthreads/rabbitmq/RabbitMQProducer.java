package com.cs.queuedvthreads.rabbitmq;

import com.cs.queuedvthreads.entity.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RabbitMQProducer {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Value("${cs.rabbitmq.exchange}")
    private String exchange;

    @Value("${cs.rabbitmq.routing-key}")
    private String routingKey;

    public void send(Message message) {
        log.info("Sending message, {}", message);

        rabbitTemplate.convertAndSend(exchange, routingKey, message);

        log.info("Message is sent successfully, {}", message);
    }
}