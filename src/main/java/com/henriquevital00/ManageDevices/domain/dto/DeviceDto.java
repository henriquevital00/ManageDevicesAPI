package com.henriquevital00.ManageDevices.domain.dto;

import com.henriquevital00.ManageDevices.domain.enums.DeviceStateEnum;

public record DeviceDto(
        Long id,
        String name,
        String brand,
        DeviceStateEnum state
) {}