package com.robomind.robot_management_service.robot.service;

import java.util.List;
import java.util.UUID;

import com.robomind.robot_management_service.exceptions.errors.RobotConflictException;
import com.robomind.robot_management_service.exceptions.errors.RobotNotFoundException;
import com.robomind.robot_management_service.robot.dto.ChangeStatusDTO;
import com.robomind.robot_management_service.robot.dto.UpdateRobotRequest;
import com.robomind.robot_management_service.robot.enums.RobotStatus;
import com.robomind.robot_management_service.robot.metrics.RobotMetrics;
import com.robomind.robot_management_service.robot.producer.RobotProducer;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.robomind.robot_management_service.robot.dto.CreateRobotRequest;
import com.robomind.robot_management_service.robot.dto.RobotResponse;
import com.robomind.robot_management_service.robot.model.Robot;
import com.robomind.robot_management_service.robot.repository.RobotRepository;

@Slf4j
@Service
@Transactional
public class RobotService {

	private final RobotRepository robotRepository;
    private final RobotProducer robotProducer;

    private final RobotMetrics robotMetrics;

	public RobotService(RobotRepository robotRepository, RobotProducer robotProducer, RobotMetrics robotMetrics) {
		this.robotRepository = robotRepository;
        this.robotProducer = robotProducer;
        this.robotMetrics = robotMetrics;
	}

	public RobotResponse create(CreateRobotRequest request) {
        log.info("Creating robot with model: {}", request.model());
        verifyIfRobotExistsBySerialNumber(request.serialNumber());
		Robot robot = buildRobot(request);

        log.info("Saving robot with id: {} and model: {}", robot.getRobotId(), robot.getModel());
		Robot savedRobot = robotRepository.save(robot);
        robotMetrics.countRobotCreated();

        var response = toResponse(savedRobot);
        log.info("Starting to publish robot created event for robot with id: {} and model: {}", response.robotId(), response.model());
        robotProducer.publishRobotCreated(response);

        log.info("Robot created event published for robot with id: {} and model: {}", response.robotId(), response.model());
        return response;
	}


	@Transactional(readOnly = true)
	public Page<RobotResponse> findAll(Pageable pageable) {
        log.info("Finding all robots with pageable: {}", pageable);
		return robotRepository.findAll(pageable).map(RobotService::toResponse);
	}

	@Transactional(readOnly = true)
	public RobotResponse findByRobotId(String id) {
        log.info("Finding robot by id: {}", id);
		return robotRepository.findByRobotId(id)
				.map(RobotService::toResponse)
				.orElseThrow(() -> {
                    log.warn("Robot not found with id: {}", id);
                    return new RobotNotFoundException("Robot not found with id: " + id);
                });
	}
    private Robot buildRobot(CreateRobotRequest request){
        log.info("Building robot with model: {}", request.model());
        return Robot.builder().robotId(UUID.randomUUID().toString()).name(request.name()).model(request.model()).serialNumber(request.serialNumber()).status(RobotStatus.ACTIVE.toString()).build();
    }
    private void verifyIfRobotExistsBySerialNumber(String serialNumber){
        log.info("Verifying if robot exists by serial number: {}", serialNumber);
        if (robotRepository.existsBySerialNumber(serialNumber)) {
            log.warn("robot already exists by serial number: {}", serialNumber);
            throw new RobotConflictException("Serial number already registered");
        }
    }
	private static RobotResponse toResponse(Robot robot) {
        log.info("Converting robot to response: {}", robot);
		return new RobotResponse(
				robot.getRobotId(),
				robot.getName(),
				robot.getModel(),
				robot.getSerialNumber(),
				robot.getStatus(),
				robot.getCreatedAt(),
				robot.getUpdatedAt());
	}

    public RobotResponse updateByRobotId(String id, UpdateRobotRequest request) {
        log.info("Updating robot with id: {}", id);
        Robot robot = robotRepository.findByRobotId(id)
                .orElseThrow(() -> {
                    log.warn("Robot not found with id: {}", id);
                    return new RobotNotFoundException("Robot not found with id: " + id);
                });
        if (request.serialNumber() != null && !robot.getSerialNumber().equals(request.serialNumber())) {
            log.info("Verifying serial number: {}", request.serialNumber());
            verifyIfRobotExistsBySerialNumber(request.serialNumber());
        }

        buildUpdateRobot(request, robot);

        log.info("Updating robot with id: {}", id);
        Robot updatedRobot = robotRepository.save(robot);
        log.info("Create updatedRobot metric for robot with id: {}", id);
        robotMetrics.countRobotUpdated();
        log.info("Robot updated metric for robot status with id: {}", id);
        robotMetrics.changeStatus(updatedRobot.getStatus());
        log.info("Robot updated with id: {}", id);
        var response = toResponse(updatedRobot);
        log.info("Finished updating robot with id: {}", id);
        log.info("Starting to publish robot updated event for robot with id: {}", id);

        robotProducer.publishRobotUpdated(response);
        log.info("Finished publishing robot updated event for robot with id: {}", id);
        return response;
    }

    private void buildUpdateRobot(UpdateRobotRequest request, Robot robot){
        log.info("Starting to update robot with id: {}", robot.getRobotId());

        if (request.name() != null && !request.name().isEmpty()) {
            log.info("Updating robot name to: {}", request.name());
            robot.setName(request.name().toLowerCase());

        }

        if (request.model() != null && !request.model().isEmpty()) {
            log.info("Updating robot model to: {}", request.model());
            robot.setModel(request.model().toLowerCase());

        }

        if (request.status() != null && !request.status().isEmpty()) {
            log.info("Updating robot status to: {}", request.status());
            robot.setStatus(request.status().toLowerCase());

        }

        if (request.serialNumber() != null && !request.serialNumber().isEmpty()) {
            log.info("Updating robot serial number to: {}", request.serialNumber());
            robot.setSerialNumber(request.serialNumber().toLowerCase());

        }
    }

    public RobotResponse changeStatus(String id, ChangeStatusDTO data) {
        log.info("Changing status of robot with id: {}", id);
        Robot robot = robotRepository.findByRobotId(id)
                .orElseThrow(() -> {
                    log.warn("Robot not found with id: {}", id);
                    return new RobotNotFoundException("Robot not found with id: " + id);
                });
        log.info("Changing status to: {}", data.status());
        robot.setStatus(data.status().toLowerCase());

        log.info("Updating robot status to: {}", data.status());
        Robot updatedRobot = robotRepository.save(robot);

        log.info("Create changeStatus metric for robot with id: {}", id);
        robotMetrics.changeStatus(data.status());

        var response = toResponse(updatedRobot);

        log.info("Finished changing status of robot with id: {}", id);
        robotProducer.publishRobotChangeStatus(response);
        log.info("Finished publishing robot change status event for robot with id: {}", id);
        return response;
    }

    public void deleteByRobotId(String id) {
        log.info("Deleting robot with id: {}", id);
        Robot robot = robotRepository.findByRobotId(id)
                .orElseThrow(() -> {
                    log.warn("Robot not found with id: {}", id);
                    return new RobotNotFoundException("Robot not found with id: " + id);
                });

        log.info("Publishing robot with id: {}", id);
        robotProducer.publishRobotDeleted(toResponse(robot));
        log.info("Finished publishing robot deleted event for robot with id: {}", id);
        robotRepository.delete(robot);
        log.info("Create robotDeleted metric for robot with id: {}", id);
        robotMetrics.countRobotDeleted();
        log.info("Finished deleting robot with id: {}", id);
    }
}
