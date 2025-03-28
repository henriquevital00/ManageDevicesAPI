package com.henriquevital00.ManageDevices.services;

import com.henriquevital00.ManageDevices.domain.dto.DeviceCreateDto;
import com.henriquevital00.ManageDevices.domain.dto.DeviceDto;
import com.henriquevital00.ManageDevices.domain.entity.Device;
import com.henriquevital00.ManageDevices.domain.enums.DeviceStateEnum;
import com.henriquevital00.ManageDevices.exception.DeviceInUseException;
import com.henriquevital00.ManageDevices.exception.ResourceNotFoundException;
import com.henriquevital00.ManageDevices.mapper.DeviceMapper;
import com.henriquevital00.ManageDevices.repository.DeviceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
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
        device.setName(deviceCreateDto.name().toUpperCase());
        device.setBrand(deviceCreateDto.brand().toUpperCase());
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

    @Override
    public List<DeviceDto> getDevicesByBrand(String brand) {
        String searchBrand = brand.toUpperCase();
        List<Device> devices = deviceRepository.getDeviceByBrand(searchBrand);
        if (devices.isEmpty()) {
            throw new ResourceNotFoundException("brand", searchBrand);
        }

        return devices.stream()
                .map(deviceMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeviceDto> getDevicesByState(DeviceStateEnum state) {
        List<Device> devices = deviceRepository.getDeviceByState(state);
        if (devices.isEmpty()) {
            throw new ResourceNotFoundException("state", state.toString());
        }

        return devices.stream()
                .map(deviceMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public DeviceDto updateDevice(Long id, DeviceCreateDto deviceCreateDto) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("id", id.toString()));
        if (device.getState() == DeviceStateEnum.IN_USE) {
            device.setState(deviceCreateDto.state());
        } else {
            device.setName(deviceCreateDto.name());
            device.setBrand(deviceCreateDto.brand());
            device.setState(deviceCreateDto.state());
        }

        deviceRepository.save(device);
        return deviceMapper.toDto(device);
    }

    @Override
    public void deleteDevice(Long id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("id", id.toString()));

        if (device.getState() == DeviceStateEnum.IN_USE) {
            throw new DeviceInUseException("Device is currently in use and cannot be deleted");
        }

        deviceRepository.delete(device);
    }
}
