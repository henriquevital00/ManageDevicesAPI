package com.henriquevital00.ManageDevices.repository;

import com.henriquevital00.ManageDevices.domain.dto.DeviceDto;
import com.henriquevital00.ManageDevices.domain.entity.Device;
import com.henriquevital00.ManageDevices.domain.enums.DeviceStateEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository  extends JpaRepository<Device, Long> {
    DeviceDto getDeviceById(Long id);
    Page<Device> getDeviceByBrand(String brand, Pageable pageable);
    Page<Device> getDeviceByState(DeviceStateEnum state, Pageable pageable);
    Page<Device> findAll(Pageable pageable);
}
