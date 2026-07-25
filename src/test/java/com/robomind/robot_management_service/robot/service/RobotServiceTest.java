package com.robomind.robot_management_service.robot.service;

import com.robomind.robot_management_service.robot.dto.CreateRobotRequest;
import com.robomind.robot_management_service.robot.dto.RobotResponse;
import com.robomind.robot_management_service.robot.model.Robot;
import com.robomind.robot_management_service.robot.repository.RobotRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class RobotServiceTest {

    @Mock
    private RobotRepository robotRepository;

    @InjectMocks
    private RobotService robotService;

    private CreateRobotRequest createRobotRequest;
    private RobotResponse robotResponse;
    private Robot robot;

    @BeforeEach
    public void setup() {
        createRobotRequest = CreateRobotRequest.builder()
                .name("Test Robot")
                .model("Model X")
                .serialNumber("SN123456")
                .build();

        robotResponse = RobotResponse.builder()
                .robotId("123456789")
                .name("Test Robot")
                .model("Model X")
                .serialNumber("SN123456")
                .status("ACTIVE")
                .createdAt(java.time.Instant.now())
                .updatedAt(java.time.Instant.now())
                .build();

        robot = Robot.builder()
                .id("123456789")
                .robotId(robotResponse.robotId())
                .name(robotResponse.name())
                .model(robotResponse.model())
                .serialNumber(robotResponse.serialNumber())
                .status(robotResponse.status())
                .createdAt(robotResponse.createdAt())
                .updatedAt(robotResponse.updatedAt())
                .build();
    }


    @Test
    @DisplayName("Should create new Robot")
    void shouldCreateNewRobot() {

        Mockito.when(robotRepository.existsBySerialNumber(createRobotRequest.serialNumber())).thenReturn(false);
        Mockito.when(robotRepository.save(Mockito.any())).thenReturn(robot);

        var result = robotService.create(createRobotRequest);

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.robotId());
        Assertions.assertEquals(robotResponse.createdAt(), result.createdAt());
        Assertions.assertEquals(robotResponse.updatedAt(), result.updatedAt());
        Assertions.assertEquals(robotResponse.name(), result.name());
        Assertions.assertEquals(robotResponse.model(), result.model());
        Assertions.assertEquals(robotResponse.serialNumber(), result.serialNumber());
        Assertions.assertEquals(robotResponse.status(), result.status());

        Mockito.verify(robotRepository, Mockito.times(1)).existsBySerialNumber(createRobotRequest.serialNumber());
        Mockito.verify(robotRepository, Mockito.times(1)).save(Mockito.any());

    }

    @Test
    @DisplayName("Should return CONFLICT if Serial number already registered for new Robot")
    void shouldReturnConflictIfSerialNumberAlreadyRegisteredForNewRobot() {

        Mockito.when(robotRepository.existsBySerialNumber(createRobotRequest.serialNumber())).thenReturn(true);

        var result = Assertions.assertThrows(ResponseStatusException.class, () -> robotService.create(createRobotRequest));

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getMessage());
        Assertions.assertEquals("409 CONFLICT \"Serial number already registered\"", result.getMessage());

        Mockito.verify(robotRepository, Mockito.times(1)).existsBySerialNumber(createRobotRequest.serialNumber());
        Mockito.verify(robotRepository, Mockito.times(0)).save(Mockito.any());

    }

    @Test
    @DisplayName("Should return paginated robots")
    void findAll() {
        Mockito.when(robotRepository.findAll(Mockito.any(Pageable.class))).thenReturn(new PageImpl<>(List.of(robot)));

        var result = robotService.findAll(Pageable.ofSize(1));

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.getTotalElements());
        Assertions.assertEquals(robotResponse.robotId(), result.getContent().getFirst().robotId());
        Assertions.assertEquals(robotResponse.name(), result.getContent().getFirst().name());
        Assertions.assertEquals(robotResponse.model(), result.getContent().getFirst().model());
        Assertions.assertEquals(robotResponse.serialNumber(), result.getContent().getFirst().serialNumber());
        Assertions.assertEquals(robotResponse.status(), result.getContent().getFirst().status());

        Mockito.verify(robotRepository, Mockito.times(1)).findAll(Mockito.any(Pageable.class));
    }

    @Test
    @DisplayName("Should return robot by ID")
    void shouldReturnRobotById() {
        Mockito.when(robotRepository.findByRobotId(Mockito.anyString())).thenReturn(Optional.of(robot));

        var result = robotService.findByRobotId("some-id");

        Assertions.assertNotNull(result);
        Assertions.assertEquals(robotResponse.robotId(), result.robotId());
        Assertions.assertEquals(robotResponse.name(), result.name());
        Assertions.assertEquals(robotResponse.model(), result.model());
        Assertions.assertEquals(robotResponse.serialNumber(), result.serialNumber());
        Assertions.assertEquals(robotResponse.status(), result.status());

        Mockito.verify(robotRepository, Mockito.times(1)).findByRobotId(Mockito.anyString());
    }

    @Test
    @DisplayName("Should return NOT FOUND if robot does not exist")
    void shouldReturnNotFoundIfRobotDoesNotExist() {
        Mockito.when(robotRepository.findByRobotId(Mockito.anyString())).thenReturn(Optional.empty());

        var result = Assertions.assertThrows(ResponseStatusException.class, () -> robotService.findByRobotId("some-id"));

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getMessage());
        Assertions.assertEquals("404 NOT_FOUND \"Robot not found\"", result.getMessage());

        Mockito.verify(robotRepository, Mockito.times(1)).findByRobotId(Mockito.anyString());
    }
}