package com.robomind.robot_management_service.robot.service;

import java.util.List;
import java.util.UUID;

import com.robomind.robot_management_service.exceptions.errors.RobotConflictException;
import com.robomind.robot_management_service.exceptions.errors.RobotNotFoundException;
import com.robomind.robot_management_service.robot.dto.UpdateRobotRequest;
import com.robomind.robot_management_service.robot.enums.RobotStatus;
import jakarta.validation.Valid;
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

@Service
@Transactional
public class RobotService {

	private final RobotRepository robotRepository;

	public RobotService(RobotRepository robotRepository) {
		this.robotRepository = robotRepository;
	}

	public RobotResponse create(CreateRobotRequest request) {
        verifyIfRobotExistsBySerialNumber(request.serialNumber());
		Robot robot = buildRobot(request);
		return toResponse(robotRepository.save(robot));
	}


	@Transactional(readOnly = true)
	public Page<RobotResponse> findAll(Pageable pageable) {
		return robotRepository.findAll(pageable).map(RobotService::toResponse);
	}

	@Transactional(readOnly = true)
	public RobotResponse findByRobotId(String id) {
		return robotRepository.findByRobotId(id)
				.map(RobotService::toResponse)
				.orElseThrow(() -> new RobotNotFoundException("Robot not found with id: " + id));
	}
    private Robot buildRobot(CreateRobotRequest request){
        return Robot.builder().robotId(UUID.randomUUID().toString()).name(request.name()).model(request.model()).serialNumber(request.serialNumber()).status(RobotStatus.ACTIVE.toString()).build();
    }
    private void verifyIfRobotExistsBySerialNumber(String serialNumber){
        if (robotRepository.existsBySerialNumber(serialNumber)) {
            throw new RobotConflictException("Serial number already registered");
        }
    }
	private static RobotResponse toResponse(Robot robot) {
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
        Robot robot = robotRepository.findByRobotId(id)
                .orElseThrow(() -> new RobotNotFoundException("Robot not found with id: " + id));
        if (request.serialNumber() != null && !robot.getSerialNumber().equals(request.serialNumber())) {
            verifyIfRobotExistsBySerialNumber(request.serialNumber());
        }

        buildUpdateRobot(request, robot);
        return toResponse(robotRepository.save(robot));
    }

    private void buildUpdateRobot(UpdateRobotRequest request, Robot robot){
        if (request.name() != null && !request.name().isEmpty()) {
            robot.setName(request.name().toLowerCase());

        }

        if (request.model() != null && !request.model().isEmpty()) {
            robot.setModel(request.model().toLowerCase());

        }

        if (request.status() != null && !request.status().isEmpty()) {
            robot.setStatus(request.status().toLowerCase());

        }

        if (request.serialNumber() != null && !request.serialNumber().isEmpty()) {
            robot.setSerialNumber(request.serialNumber().toLowerCase());

        }
    }

    public RobotResponse inactivateByRobotId(String id) {
        Robot robot = robotRepository.findByRobotId(id)
                .orElseThrow(() -> new RobotNotFoundException("Robot not found with id: " + id));
        robot.setStatus(RobotStatus.INACTIVE.toString());
        return toResponse(robotRepository.save(robot));
    }
}
