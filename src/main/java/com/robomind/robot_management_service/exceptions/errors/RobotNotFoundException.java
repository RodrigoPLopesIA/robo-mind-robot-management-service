package com.robomind.robot_management_service.exceptions.errors;

public class RobotNotFoundException extends RuntimeException {

    public RobotNotFoundException(String message) {
        super(message);
    }
}
