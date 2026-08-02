package com.robomind.robot_management_service.robot.controller;

import java.util.List;
import java.util.UUID;

import com.robomind.robot_management_service.robot.dto.ChangeStatusDTO;
import com.robomind.robot_management_service.robot.dto.UpdateRobotRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Create a new robot", description = "Creates a new robot with the provided details.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Robot created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request data"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Robot with the same serial number already exists")
    })
	@PostMapping
	public ResponseEntity<RobotResponse> create(@Valid @RequestBody CreateRobotRequest request) {
        log.info("Creating robot with model: {}", request.model());
		return ResponseEntity.status(HttpStatus.CREATED).body(robotService.create(request));
	}

    @Operation(summary = "Get all robots", description = "Retrieves a paginated list of all robots.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved list of robots")
    })
	@GetMapping
	public ResponseEntity<Page<RobotResponse>> findAll(Pageable pageable) {
        log.info("Finding all robots with pageable: {}", pageable);
		return ResponseEntity.ok().body(robotService.findAll(pageable));
	}

    @Operation(summary = "Get robot by ID", description = "Retrieves a robot by its unique ID.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved robot"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Robot not found")
    })
	@GetMapping("/{id}")
	public ResponseEntity<RobotResponse> findById(@PathVariable String id) {
        log.info("Finding robot with robot id: {}", id);
		return ResponseEntity.ok().body(robotService.findByRobotId(id));
	}

    @Operation(summary = "Update robot by ID", description = "Updates a robot by its unique ID.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully updated robot"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Robot not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<RobotResponse> update(@PathVariable String id, @Valid @RequestBody UpdateRobotRequest request) {
        log.info("Updating robot with robot id and model: {} -- {}", id,  request.model());
        return ResponseEntity.ok().body(robotService.updateByRobotId(id, request));
    }

    @Operation(summary = "Change robot status by ID", description = "Changes the status of a robot by its unique ID.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully changed robot status"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Robot not found")
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<RobotResponse> changeStatus(@PathVariable String id, @Valid @RequestBody ChangeStatusDTO data) {
        log.info("Changing status of robot with robot id and status: {} -- {}", id,  data.status());
        return ResponseEntity.ok().body(robotService.changeStatus(id, data));
    }

    @Operation(summary = "Delete robot by ID", description = "Deletes a robot by its unique ID.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Successfully deleted robot"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Robot not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        log.info("Deleting robot with robot id: {}", id);
        robotService.deleteByRobotId(id);
        return ResponseEntity.noContent().build();
    }
}
