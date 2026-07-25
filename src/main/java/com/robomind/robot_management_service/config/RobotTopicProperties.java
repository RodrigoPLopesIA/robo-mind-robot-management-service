package com.robomind.robot_management_service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties(prefix = "topics.robot")
public class RobotTopicProperties {

    private String created;
    private String updated;
    private String changeStatus;
    private String deleted;

}
