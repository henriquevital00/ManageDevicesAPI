package com.henriquevital00.ManageDevices.repository;

import com.henriquevital00.ManageDevices.domain.dto.DeviceDto;
import com.henriquevital00.ManageDevices.domain.entity.Device;
import com.henriquevital00.ManageDevices.domain.enums.DeviceStateEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository  extends JpaRepository<Device, Long> {
    List<DeviceDto> getDevicesByBrand(String brand);
    List<DeviceDto> getDevicesByState(String state);
    DeviceDto getDeviceById(Long id);
    List<Device> getDeviceByBrand(String brand);
    List<Device> getDeviceByState(DeviceStateEnum state);
}
