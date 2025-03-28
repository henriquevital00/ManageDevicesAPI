package com.henriquevital00.ManageDevices.domain.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DeviceStateValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDeviceState {
    String message() default "Invalid device state";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
