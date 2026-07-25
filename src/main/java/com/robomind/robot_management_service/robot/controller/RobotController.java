package com.robomind.robot_management_service.robot.controller;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.robomind.robot_management_service.robot.dto.CreateRobotRequest;
import com.robomind.robot_management_service.robot.dto.RobotResponse;
import com.robomind.robot_management_service.robot.service.RobotService;

@RestController
@RequestMapping("/robots")
public class RobotController {

	private final RobotService robotService;

	public RobotController(RobotService robotService) {
		this.robotService = robotService;
	}

	@PostMapping
	public ResponseEntity<RobotResponse> create(@Valid @RequestBody CreateRobotRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(robotService.create(request));
	}

	@GetMapping
	public Page<RobotResponse> findAll(Pageable pageable) {
		return robotService.findAll(pageable);
	}

	@GetMapping("/{id}")
	public RobotResponse findById(@PathVariable String id) {
		return robotService.findByRobotId(id);
	}
}
