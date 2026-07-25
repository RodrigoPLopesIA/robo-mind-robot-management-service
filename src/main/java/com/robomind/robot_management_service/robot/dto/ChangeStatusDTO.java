package com.robomind.robot_management_service.robot.dto;

import com.robomind.robot_management_service.robot.enums.RobotStatus;
import com.robomind.robot_management_service.robot.enums.validation_decorator.RobotEnumValidation;

public record ChangeStatusDTO(@RobotEnumValidation(enumClass = RobotStatus.class) String status) {
}
