package com.henriquevital00.ManageDevices.services;

import com.henriquevital00.ManageDevices.domain.dto.DeviceCreateDto;
import com.henriquevital00.ManageDevices.domain.dto.DeviceDto;

import java.util.List;

public interface DeviceService {
    DeviceDto createDevice(DeviceCreateDto deviceCreateDto);
    List<DeviceDto> getAllDevices();
}
