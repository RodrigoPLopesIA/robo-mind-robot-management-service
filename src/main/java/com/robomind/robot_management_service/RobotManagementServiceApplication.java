package com.robomind.robot_management_service;

import com.robomind.robot_management_service.config.RobotTopicProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
@EnableConfigurationProperties(RobotTopicProperties.class)
public class RobotManagementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RobotManagementServiceApplication.class, args);
	}

}
