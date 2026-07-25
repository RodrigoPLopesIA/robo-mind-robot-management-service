package com.robomind.robot_management_service.robot.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CreateRobotRequest(
		@NotBlank String name,
		@NotBlank String model,
		@NotBlank String serialNumber) {
}
