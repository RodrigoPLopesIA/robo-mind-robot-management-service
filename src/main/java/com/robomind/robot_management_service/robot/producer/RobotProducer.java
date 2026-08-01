package com.robomind.robot_management_service.robot.producer;

import com.robomind.robot_management_service.config.RobotTopicProperties;
import com.robomind.robot_management_service.producer.Producer;
import com.robomind.robot_management_service.robot.dto.RobotResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RobotProducer extends Producer {

    private final RobotTopicProperties topics;

    public RobotProducer(KafkaTemplate<String, String> kafkaTemplate, RobotTopicProperties topics) {
        super(kafkaTemplate);
        this.topics = topics;
    }

    public void publishRobotCreated(Object message) {
        log.info("Publishing robot created event to topic: {}", topics.getCreated());
        publish(topics.getCreated(), message);
    }

    public void publishRobotUpdated(Object message) {
        log.info("Publishing robot updated event to topic: {}", topics.getUpdated());
        publish(topics.getUpdated(), message);
    }

    public void publishRobotChangeStatus(Object message) {
        log.info("Publishing robot change status event to topic: {}", topics.getChangeStatus());
        publish(topics.getChangeStatus(), message);
    }

    public void publishRobotDeleted(RobotResponse response) {
        log.info("Publishing robot deleted event to topic: {}", topics.getDeleted());
        publish(topics.getDeleted(), response);
    }
}
