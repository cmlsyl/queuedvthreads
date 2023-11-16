package com.cs.queuedvthreads.rabbitmq;

import com.cs.queuedvthreads.entity.Message;
import com.cs.queuedvthreads.task.MessageManager;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQConsumer {
    private final MessageManager messageManager;
    private final RabbitTemplate rabbitTemplate;

    @Value("${cs.rabbitmq.queue.retry.limit}")
    private Integer retryLimit;

    @Value("${cs.rabbitmq.queue.unprocessed.name}")
    private String unprocessedQueue;

    @RabbitListener(queues = "cs.queue.message", ackMode = "MANUAL")
    public void consume(Message message,
                        Channel channel,
                        @Header(AmqpHeaders.DELIVERY_TAG) long tag,
                        @Header(required = false, name = "x-death") Map<String, ?> xDeath) throws Exception {
        log.info("Received message: {}", message);

        if (isReachedRetryCount(xDeath)) {
            sendToUnprocessed(message);
            channel.basicAck(tag, false);
        } else {
            messageManager.process(message, channel, tag);
        }
    }

    private boolean isReachedRetryCount(Map<String, ?> xDeath) {
        Long count = 0L;

        if (xDeath != null && !xDeath.isEmpty()) {
            count = (Long) xDeath.get("count");
        }

        return count >= retryLimit;
    }

    private void sendToUnprocessed(Message message) {
        log.warn("Maximum retry limit reached, send message to the unprocessed queue, message: {}", message);
        rabbitTemplate.convertAndSend(unprocessedQueue, message);
    }
}
