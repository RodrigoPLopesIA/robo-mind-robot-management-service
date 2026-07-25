package com.robomind.robot_management_service.robot.dto;

import lombok.Builder;

import java.time.Instant;

@Builder
public record RobotResponse(
		String robotId,
		String name,
		String model,
		String serialNumber,
		String status,
		Instant createdAt,
		Instant updatedAt) {
}
