package com.robomind.robot_management_service.robot.enums.validation_decorator;

import com.robomind.robot_management_service.robot.enums.validator.RobotEnumValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RobotEnumValidator.class)
public @interface RobotEnumValidation {

    Class<? extends Enum<?>> enumClass();

    String message() default "Invalid value for robot status should be active or inactive";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
