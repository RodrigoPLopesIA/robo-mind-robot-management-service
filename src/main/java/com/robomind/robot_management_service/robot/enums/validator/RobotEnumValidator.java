package com.robomind.robot_management_service.robot.enums.validator;

import com.robomind.robot_management_service.robot.enums.RobotStatus;
import com.robomind.robot_management_service.robot.enums.validation_decorator.RobotEnumValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class RobotEnumValidator implements ConstraintValidator<RobotEnumValidation, String> {

    private Set<String> acceptedValues;

    @Override
    public void initialize(RobotEnumValidation annotation) {
        log.info("Initializing RobotEnumValidator");
        acceptedValues = Arrays.stream(annotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toSet());
        log.info("Accepted values: {}", acceptedValues);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        log.info("Validating value: {} against accepted values: {}", value, acceptedValues);
        return value == null || acceptedValues.contains(value.toUpperCase(Locale.ROOT));
    }
}
