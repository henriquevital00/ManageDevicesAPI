package com.henriquevital00.ManageDevices.domain.validation;

import com.henriquevital00.ManageDevices.domain.enums.DeviceStateEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DeviceStateValidator implements ConstraintValidator<ValidDeviceState, DeviceStateEnum> {

    private String message;

    @Override
    public void initialize(ValidDeviceState constraintAnnotation) {
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(DeviceStateEnum value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        for (DeviceStateEnum state : DeviceStateEnum.values()) {
            if (state == value) {
                return true;
            }
        }
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
        return false;
    }
}
