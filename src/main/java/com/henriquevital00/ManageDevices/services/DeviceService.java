package com.henriquevital00.ManageDevices.services;

import com.henriquevital00.ManageDevices.domain.dto.DeviceCreateDto;
import com.henriquevital00.ManageDevices.domain.dto.DeviceDto;
import com.henriquevital00.ManageDevices.domain.enums.DeviceStateEnum;

import java.util.List;
import java.util.Map;

public interface DeviceService {
    DeviceDto createDevice(DeviceCreateDto deviceCreateDto);
    List<DeviceDto> getAllDevices(int page, int size);
    DeviceDto getDeviceById(Long id);
    List<DeviceDto> getDevicesByBrand(String brand, int page, int size);
    List<DeviceDto> getDevicesByState(DeviceStateEnum state, int page, int size);
    DeviceDto updateDevice(Long id, DeviceCreateDto deviceCreateDto);
    void deleteDevice(Long id);
}
