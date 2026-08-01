package com.robomind.robot_management_service.exceptions.errors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RobotNotFoundException extends RuntimeException {

    public RobotNotFoundException(String message) {
        super(message);
        log.error("RobotNotFoundException",  message);
    }
}
