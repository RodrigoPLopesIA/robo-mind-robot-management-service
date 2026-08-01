package com.robomind.robot_management_service.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import tools.jackson.databind.ObjectMapper;

@Slf4j
public abstract class Producer implements PublishMessage {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public Producer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void publish(String topic, Object message) {
        log.info("Start publishing message to topic: {} with message: {}", topic, message);
        try {
            log.info("Publishing message to topic: {} with message: {}", topic, message);
            String jsonMessage = convertValue(message);
            kafkaTemplate.send(topic, jsonMessage);
            log.info("Message published to topic: {} with message: {}", topic, jsonMessage);
        } catch (Exception e) {
            log.error("Error publishing message to topic: {} with message: {}", topic, message, e);
            e.printStackTrace();
        }
    }


    private String convertValue(Object value) throws Exception {
        log.info("Converting object to json string: {}", value);
        return objectMapper.writeValueAsString(value);
    }
}
