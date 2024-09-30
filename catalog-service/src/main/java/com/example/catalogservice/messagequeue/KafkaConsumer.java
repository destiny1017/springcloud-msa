package com.example.catalogservice.messagequeue;

import com.example.catalogservice.jpa.CatalogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumer {

    private final CatalogRepository repository;

    @Autowired
    public KafkaConsumer(CatalogRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(topics = "topic1", containerFactory = "kafkaListenerContainerFactory")
    public void updateQty(String kafkaMessage) {
//        log.info("Kafka Message: ->" + kafkaMessage);
//
//        Map<Object, Object> map = new HashMap<>();
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            map = mapper.readValue(kafkaMessage, new TypeReference<>() {});
//        } catch (JsonProcessingException ex) {
//            ex.printStackTrace();
//        }
//
//        CatalogEntity entity = repository.findByProductId((String)map.get("productId")).get();
//
//        if (entity != null) {
//            entity.updateStock(entity.getStock() - (Integer) map.get("qty"));
//            repository.save(entity);
//        }
    }
}
