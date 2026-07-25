package com.robomind.robot_management_service.exceptions.handler;


import com.robomind.robot_management_service.exceptions.dtos.ResponseErrorDTO;
import com.robomind.robot_management_service.exceptions.errors.RobotConflictException;
import com.robomind.robot_management_service.exceptions.errors.RobotNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerGlobalErrorHandler {

    @ExceptionHandler(RobotConflictException.class)
    public ResponseEntity<ResponseErrorDTO> handleRobotConflictException(RobotConflictException ex, HttpServletRequest request) {
        ResponseErrorDTO responseErrorDTO = ResponseErrorDTO.builder()
                .path(request.getRequestURI())
                .message(ex.getMessage())
                .status(409)
                .errors(null)
                .build();
        return ResponseEntity.status(409).body(responseErrorDTO);
    }

    @ExceptionHandler(RobotNotFoundException.class)
    public ResponseEntity<ResponseErrorDTO> handleRobotNotFoundException(RobotNotFoundException ex, HttpServletRequest request) {
        ResponseErrorDTO response = ResponseErrorDTO.builder()
                .path(request.getRequestURI())
                .message(ex.getMessage())
                .status(404)
                .errors(null)
                .build();
        return ResponseEntity.status(404).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseErrorDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        ResponseErrorDTO response = ResponseErrorDTO.builder()
                .path(request.getRequestURI())
                .message("Validation failed")
                .status(400)
                .errors(errors)
                .build();
        return ResponseEntity.status(400).body(response);
    }
}
