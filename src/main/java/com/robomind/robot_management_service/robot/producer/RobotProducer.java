package com.robomind.robot_management_service.robot.producer;

import com.robomind.robot_management_service.config.RobotTopicProperties;
import com.robomind.robot_management_service.producer.Producer;
import com.robomind.robot_management_service.robot.dto.RobotResponse;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class RobotProducer extends Producer {

    private final RobotTopicProperties topics;

    public RobotProducer(KafkaTemplate<String, String> kafkaTemplate, RobotTopicProperties topics) {
        super(kafkaTemplate);
        this.topics = topics;
    }

    public void publishRobotCreated(Object message) {
        publish(topics.getCreated(), message);
    }

    public void publishRobotUpdated(Object message) {
        publish(topics.getUpdated(), message);
    }

    public void publishRobotChangeStatus(Object message) {
        publish(topics.getChangeStatus(), message);
    }

    public void publishRobotDeleted(RobotResponse response) {

    }
}
