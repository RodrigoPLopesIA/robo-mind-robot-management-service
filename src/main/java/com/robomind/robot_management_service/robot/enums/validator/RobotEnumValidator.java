package com.robomind.robot_management_service.robot.enums.validator;

import com.robomind.robot_management_service.robot.enums.RobotStatus;
import com.robomind.robot_management_service.robot.enums.validation_decorator.RobotEnumValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public class RobotEnumValidator implements ConstraintValidator<RobotEnumValidation, String> {

    private Set<String> acceptedValues;

    @Override
    public void initialize(RobotEnumValidation annotation) {
        acceptedValues = Arrays.stream(annotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || acceptedValues.contains(value.toUpperCase(Locale.ROOT));
    }
}
