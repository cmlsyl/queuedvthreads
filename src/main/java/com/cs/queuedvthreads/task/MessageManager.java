package com.cs.queuedvthreads.task;

import com.cs.queuedvthreads.entity.Message;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
public class MessageManager {
    private final ExecutorService virtualThreadExecutor;

    public MessageManager() {
        virtualThreadExecutor = Executors.newVirtualThreadPerTaskExecutor();
    }

    public void process(Message message, Channel channel, long tag) {
        virtualThreadExecutor.execute(new MessageConsumerTask(channel, tag, message));
    }
}
