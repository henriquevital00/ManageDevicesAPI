package com.henriquevital00.ManageDevices.services;

import com.henriquevital00.ManageDevices.domain.dto.DeviceCreateDto;
import com.henriquevital00.ManageDevices.domain.dto.DeviceDto;
import com.henriquevital00.ManageDevices.domain.entity.Device;
import com.henriquevital00.ManageDevices.exception.ResourceNotFoundException;
import com.henriquevital00.ManageDevices.mapper.DeviceMapper;
import com.henriquevital00.ManageDevices.repository.DeviceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DeviceServiceImpl implements  DeviceService {
    private DeviceRepository deviceRepository;
    private DeviceMapper deviceMapper;

    @Override
    public DeviceDto createDevice(DeviceCreateDto deviceCreateDto) {
        Device device = deviceMapper.toEntity(deviceCreateDto);
        Device savedDevice = deviceRepository.save(device);
        return deviceMapper.toDto(savedDevice);
    }

    @Override
    public List<DeviceDto> getAllDevices() {
        return deviceRepository.findAll().stream()
                .map(deviceMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public DeviceDto getDeviceById(Long id) {
        Optional<Device> device = deviceRepository.findById(id);
        return device.map(deviceMapper::toDto).orElseThrow(() -> new ResourceNotFoundException("id", id.toString()));
    }
}
