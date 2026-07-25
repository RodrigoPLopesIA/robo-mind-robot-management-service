package com.robomind.robot_management_service.robot.controller;

import java.util.List;
import java.util.UUID;

import com.robomind.robot_management_service.robot.dto.UpdateRobotRequest;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
	public ResponseEntity<Page<RobotResponse>> findAll(Pageable pageable) {
		return ResponseEntity.ok().body(robotService.findAll(pageable));
	}

	@GetMapping("/{id}")
	public ResponseEntity<RobotResponse> findById(@PathVariable String id) {
		return ResponseEntity.ok().body(robotService.findByRobotId(id));
	}

    @PutMapping("/{id}")
    public ResponseEntity<RobotResponse> update(@PathVariable String id, @Valid @RequestBody UpdateRobotRequest request) {
        return ResponseEntity.ok().body(robotService.updateByRobotId(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RobotResponse> inactivate(@PathVariable String id) {
        return ResponseEntity.ok().body(robotService.inactivateByRobotId(id));
    }
}
