package com.henriquevital00.ManageDevices.domain.dto;

import com.henriquevital00.ManageDevices.domain.enums.DeviceStateEnum;
import com.henriquevital00.ManageDevices.domain.validation.ValidDeviceState;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DeviceCreateDto(
        @NotBlank(message = "Name is mandatory") String name,
        @NotBlank(message = "Brand is mandatory") String brand,
        @NotNull(message = "State is mandatory")
        @ValidDeviceState DeviceStateEnum state
) {}