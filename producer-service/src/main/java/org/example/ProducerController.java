package org.example;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProducerController {

    private final KafkaProducer kafkaProducer;

    @PostMapping("/sendMessage")
    public void sendMessage(@RequestBody String message) {
        kafkaProducer.sendMessage("topic1", message);
    }
}
