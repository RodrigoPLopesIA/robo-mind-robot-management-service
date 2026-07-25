package com.robomind.robot_management_service.robot.dto;

import com.robomind.robot_management_service.robot.enums.RobotStatus;
import com.robomind.robot_management_service.robot.enums.validation_decorator.RobotEnumValidation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UpdateRobotRequest(String name,
                                 String model,
                                 @RobotEnumValidation(enumClass = RobotStatus.class) String status,
                                 String serialNumber) {
}
