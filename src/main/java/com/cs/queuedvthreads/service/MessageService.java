package com.cs.queuedvthreads.service;

import com.cs.queuedvthreads.entity.Message;
import com.cs.queuedvthreads.rabbitmq.RabbitMQProducer;
import com.cs.queuedvthreads.repository.MessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessageService extends BaseService<Message> {
    private final RabbitMQProducer rabbitMQProducer;

    public MessageService(MessageRepository messageRepository, RabbitMQProducer rabbitMQProducer) {
        super(messageRepository);
        this.rabbitMQProducer = rabbitMQProducer;
    }

    public void persistAndQueue(Message message) {
        save(message);
        rabbitMQProducer.send(message);
    }
}
