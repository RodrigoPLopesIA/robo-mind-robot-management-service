package com.robomind.robot_management_service.robot.metrics;

import com.robomind.robot_management_service.robot.enums.RobotStatus;
import com.robomind.robot_management_service.robot.service.RobotService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class RobotMetrics {


    private final Counter  robotCreated;
    private final Counter robotUpdated;
    private final Counter robotDeleted;
    private final Counter robotInactive;
    private final Counter robotActive;

    public RobotMetrics(MeterRegistry registry) {
        this.robotCreated = Counter.builder("robot_created_total")
                .description("Total number of robots created")
                .register(registry);
        this.robotUpdated = Counter.builder("robot_updated_total")
                .description("Total number of robots updated")
                .register(registry);
        this.robotDeleted = Counter.builder("robot_deleted_total")
                .description("Total number of robots deleted")
                .register(registry);
        this.robotInactive = Counter.builder("robot_inactive_total")
                .description("Total number of robots inactive")
                .register(registry);
        this.robotActive = Counter.builder("robot_active_total")
                .description("Total number of robots active")
                .register(registry);
    }


    public void countRobotCreated(){
        robotCreated.increment();
    }
    public void countRobotUpdated(){
        robotUpdated.increment();
    }
    public void countRobotDeleted(){
        robotDeleted.increment();
    }

    public void changeStatus(String status){
        if (status.equalsIgnoreCase(RobotStatus.ACTIVE.name())) {
            countRobotActive();
        } else {
            countRobotInactive();
        }
    }
    private void countRobotInactive(){
        robotInactive.increment();
    }

    private void countRobotActive(){
        robotActive.increment();
    }
}
