package com.robomind.robot_management_service.robot.metrics;

import com.robomind.robot_management_service.robot.enums.RobotStatus;
import com.robomind.robot_management_service.robot.service.RobotService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import org.springframework.stereotype.Component;

@Component
public class RobotMetrics {


    private final Counter  robotCreated;
    private final Counter robotUpdated;
    private final Counter robotDeleted;
    private final Counter robotInactive;
    private final Counter robotActive;

    public RobotMetrics(Counter robotCreated,  Counter robotUpdated, Counter robotDeleted, Counter robotInactive, Counter robotActive) {
        this.robotCreated = robotCreated;
        this.robotUpdated = robotUpdated;
        this.robotDeleted = robotDeleted;
        this.robotInactive = robotInactive;
        this.robotActive = robotActive;
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
