package com.robomind.robot_management_service.robot.controller;

import com.robomind.robot_management_service.exceptions.handler.ControllerGlobalErrorHandler;
import com.robomind.robot_management_service.robot.dto.CreateRobotRequest;
import com.robomind.robot_management_service.robot.dto.RobotResponse;
import com.robomind.robot_management_service.robot.dto.UpdateRobotRequest;
import com.robomind.robot_management_service.robot.service.RobotService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.data.domain.PageRequest;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RobotControllerTest {

    @Mock
    private RobotService robotService;

    @InjectMocks
    private RobotController robotController;

    private MockMvc mockMvc;
    private RobotResponse robotResponse;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(robotController)
                .setControllerAdvice(new ControllerGlobalErrorHandler())
                .setValidator(validator)
                .build();

        robotResponse = RobotResponse.builder()
                .robotId("robot-123")
                .name("test robot")
                .model("model x")
                .serialNumber("sn123456")
                .status("active")
                .createdAt(Instant.parse("2026-07-25T19:31:45Z"))
                .updatedAt(Instant.parse("2026-07-25T19:31:45Z"))
                .build();
    }

    @Test
    void create() throws Exception {
        when(robotService.create(any(CreateRobotRequest.class))).thenReturn(robotResponse);

        mockMvc.perform(post("/robots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "test robot",
                                  "model": "model x",
                                  "serialNumber": "sn123456"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.robotId").value("robot-123"))
                .andExpect(jsonPath("$.name").value("test robot"))
                .andExpect(jsonPath("$.model").value("model x"))
                .andExpect(jsonPath("$.serialNumber").value("sn123456"))
                .andExpect(jsonPath("$.status").value("active"));
    }

    @Test
    void createWithInvalidBody() throws Exception {
        mockMvc.perform(post("/robots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "",
                                  "model": "",
                                  "serialNumber": ""
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors.name").exists())
                .andExpect(jsonPath("$.errors.model").exists())
                .andExpect(jsonPath("$.errors.serialNumber").exists());

        verifyNoInteractions(robotService);
    }

    @Test
    void findAll() throws Exception {
        when(robotService.findAll(any())).thenReturn(new org.springframework.data.domain.PageImpl<>(new java.util.ArrayList<>(List.of(robotResponse))));

        var response = robotController.findAll(PageRequest.of(0, 20));

        org.junit.jupiter.api.Assertions.assertEquals(200, response.getStatusCode().value());
        org.junit.jupiter.api.Assertions.assertEquals("robot-123", response.getBody().getContent().getFirst().robotId());
    }

    @Test
    void findById() throws Exception {
        when(robotService.findByRobotId("robot-123")).thenReturn(robotResponse);

        mockMvc.perform(get("/robots/robot-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.robotId").value("robot-123"))
                .andExpect(jsonPath("$.status").value("active"));
    }

    @Test
    void update() throws Exception {
        RobotResponse updatedResponse = RobotResponse.builder()
                .robotId("robot-123")
                .name("updated robot")
                .model("model y")
                .serialNumber("sn999999")
                .status("active")
                .createdAt(robotResponse.createdAt())
                .updatedAt(robotResponse.updatedAt())
                .build();

        when(robotService.updateByRobotId(anyString(), any(UpdateRobotRequest.class))).thenReturn(updatedResponse);

        mockMvc.perform(put("/robots/robot-123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "updated robot",
                                  "model": "model y",
                                  "status": "active",
                                  "serialNumber": "sn999999"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.robotId").value("robot-123"))
                .andExpect(jsonPath("$.name").value("updated robot"))
                .andExpect(jsonPath("$.model").value("model y"))
                .andExpect(jsonPath("$.serialNumber").value("sn999999"));
    }

    @Test
    void inactivate() throws Exception {
        RobotResponse inactiveResponse = RobotResponse.builder()
                .robotId("robot-123")
                .name("test robot")
                .model("model x")
                .serialNumber("sn123456")
                .status("inactive")
                .createdAt(robotResponse.createdAt())
                .updatedAt(robotResponse.updatedAt())
                .build();

        when(robotService.inactivateByRobotId("robot-123")).thenReturn(inactiveResponse);

        mockMvc.perform(delete("/robots/robot-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.robotId").value("robot-123"))
                .andExpect(jsonPath("$.status").value("inactive"));
    }
}
