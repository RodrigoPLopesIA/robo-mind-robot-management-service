package com.robomind.robot_management_service.exceptions.errors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RobotConflictException extends RuntimeException {

    public RobotConflictException(String message) {
        super(message);
        log.info("Constructing RobotConflictException: {}", message);
    }
}
