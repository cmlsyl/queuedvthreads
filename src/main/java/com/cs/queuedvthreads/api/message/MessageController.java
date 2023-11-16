package com.cs.queuedvthreads.api.message;

import com.cs.queuedvthreads.entity.Message;
import com.cs.queuedvthreads.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/message")
public class MessageController {
    private final MessageService messageService;

    @GetMapping
    public List<Message> get() {
        return messageService.findAll();
    }

    @PostMapping
    public String save(@RequestBody Message message) {
        log.info("Message arrived, {}", message);

        try {
            messageService.persistAndQueue(message);
        } catch (Exception e) {
            log.error("Error while message persist and queue: ", e);
        }

        return "Message is sent to the queue successfully";
    }
}
