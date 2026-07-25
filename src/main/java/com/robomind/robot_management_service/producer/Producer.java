package com.robomind.robot_management_service.producer;

import org.springframework.kafka.core.KafkaTemplate;
import tools.jackson.databind.ObjectMapper;

public abstract class Producer implements PublishMessage {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public Producer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void publish(String topic, Object message) {
        try {
            String jsonMessage = convertValue(message);
            kafkaTemplate.send(topic, jsonMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String convertValue(Object value) throws Exception {
        return objectMapper.writeValueAsString(value);
    }
}
