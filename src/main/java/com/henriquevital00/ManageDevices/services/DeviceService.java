package com.henriquevital00.ManageDevices.services;

import com.henriquevital00.ManageDevices.domain.dto.DeviceCreateDto;
import com.henriquevital00.ManageDevices.domain.dto.DeviceDto;
import com.henriquevital00.ManageDevices.domain.enums.DeviceStateEnum;

import java.util.List;
import java.util.Map;

public interface DeviceService {
    DeviceDto createDevice(DeviceCreateDto deviceCreateDto);
    List<DeviceDto> getAllDevices();
    DeviceDto getDeviceById(Long id);
    List<DeviceDto> getDevicesByBrand(String brand);
    List<DeviceDto> getDevicesByState(DeviceStateEnum state);
    DeviceDto updateDevice(Long id, DeviceCreateDto deviceCreateDto);
    void deleteDevice(Long id);
}
