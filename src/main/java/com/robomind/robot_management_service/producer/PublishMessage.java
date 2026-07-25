package com.robomind.robot_management_service.producer;

public interface PublishMessage {

    void publish(String topic, Object message);
}
