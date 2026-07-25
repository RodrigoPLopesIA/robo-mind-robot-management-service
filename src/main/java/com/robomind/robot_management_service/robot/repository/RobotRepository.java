package com.robomind.robot_management_service.robot.repository;

import java.nio.channels.FileChannel;
import java.util.Optional;
import java.util.UUID;
import com.robomind.robot_management_service.robot.model.Robot;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RobotRepository extends MongoRepository<Robot, UUID> {

	boolean existsBySerialNumber(String serialNumber);

    Optional<Robot> findByRobotId(String id);
}
