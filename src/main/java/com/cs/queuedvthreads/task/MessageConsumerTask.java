package com.cs.queuedvthreads.task;

import com.cs.queuedvthreads.entity.Message;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class MessageConsumerTask implements Runnable {
    private final Channel queueChannel;
    private final long queueTag;
    private final Message message;

    @Override
    public void run() {
        try {
            log.info("Processing message: {}", message);

            Thread.sleep(100);
            // TODO process

            queueChannel.basicAck(queueTag, false);

            log.info("Message process completed successfully: {}", message);
        } catch (Exception e) {
            log.error("Error when processing: ", e);

            try {
                queueChannel.basicNack(queueTag, false, false);
            } catch (Exception e1) {
                log.error("Error when rabbitmq not ack response: ", e1);
            }
        }
    }
}
