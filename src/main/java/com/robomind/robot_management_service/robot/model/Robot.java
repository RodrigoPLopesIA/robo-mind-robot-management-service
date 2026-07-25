package com.robomind.robot_management_service.robot.model;

import java.time.Instant;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@Document(collection = "robots")
public class Robot {

    @Id
    private String id;

    @Indexed(unique = true)
    private String robotId;

    private String name;
    private String model;
    @Field(name = "serial_number")
    private String serialNumber;
    private String status;
    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

}