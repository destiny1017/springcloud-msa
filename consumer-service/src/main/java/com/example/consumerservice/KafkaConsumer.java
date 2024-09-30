package com.example.consumerservice;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    @KafkaListener(topics = "topic1", groupId = "my-group")
    public void handleMessage(String message) {
        System.out.println("Received message: " + message);
    }

}