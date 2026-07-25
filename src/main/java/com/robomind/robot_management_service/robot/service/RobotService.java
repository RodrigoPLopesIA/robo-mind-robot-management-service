package com.robomind.robot_management_service.robot.service;

import java.util.List;
import java.util.UUID;

import com.robomind.robot_management_service.robot.enums.RobotStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
        verifyIfRobotExists(request);
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
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Robot not found"));
	}
    private Robot buildRobot(CreateRobotRequest request){
        return Robot.builder().robotId(UUID.randomUUID().toString()).name(request.name()).model(request.model()).serialNumber(request.serialNumber()).status(RobotStatus.ACTIVE.toString()).build();
    }
    private void verifyIfRobotExists(CreateRobotRequest request){
        if (robotRepository.existsBySerialNumber(request.serialNumber())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Serial number already registered");
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
}
