package com.robomind.robot_management_service.robot.controller;

import java.util.List;
import java.util.UUID;

import com.robomind.robot_management_service.robot.dto.ChangeStatusDTO;
import com.robomind.robot_management_service.robot.dto.UpdateRobotRequest;
import jakarta.validation.Valid;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class RobotController {

	private final RobotService robotService;

	public RobotController(RobotService robotService) {
		this.robotService = robotService;
	}

	@PostMapping
	public ResponseEntity<RobotResponse> create(@Valid @RequestBody CreateRobotRequest request) {
        log.info("Creating robot with model: {}", request.model());
		return ResponseEntity.status(HttpStatus.CREATED).body(robotService.create(request));
	}

	@GetMapping
	public ResponseEntity<Page<RobotResponse>> findAll(Pageable pageable) {
        log.info("Finding all robots with pageable: {}", pageable);
		return ResponseEntity.ok().body(robotService.findAll(pageable));
	}

	@GetMapping("/{id}")
	public ResponseEntity<RobotResponse> findById(@PathVariable String id) {
        log.info("Finding robot with robot id: {}", id);
		return ResponseEntity.ok().body(robotService.findByRobotId(id));
	}

    @PutMapping("/{id}")
    public ResponseEntity<RobotResponse> update(@PathVariable String id, @Valid @RequestBody UpdateRobotRequest request) {
        log.info("Updating robot with robot id and model: {} -- {}", id,  request.model());
        return ResponseEntity.ok().body(robotService.updateByRobotId(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<RobotResponse> changeStatus(@PathVariable String id, @Valid @RequestBody ChangeStatusDTO data) {
        log.info("Changing status of robot with robot id and status: {} -- {}", id,  data.status());
        return ResponseEntity.ok().body(robotService.changeStatus(id, data));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        log.info("Deleting robot with robot id: {}", id);
        robotService.deleteByRobotId(id);
        return ResponseEntity.noContent().build();
    }
}
