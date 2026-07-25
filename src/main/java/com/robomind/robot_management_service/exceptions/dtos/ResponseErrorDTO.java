package com.robomind.robot_management_service.exceptions.dtos;

import lombok.Builder;

import java.util.Map;

@Builder
public record ResponseErrorDTO(String path, String message, int status, Map<String, String> errors) {
}
