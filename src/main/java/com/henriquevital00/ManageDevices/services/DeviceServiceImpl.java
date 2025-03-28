package com.henriquevital00.ManageDevices.services;

import com.henriquevital00.ManageDevices.domain.dto.DeviceCreateDto;
import com.henriquevital00.ManageDevices.domain.dto.DeviceDto;
import com.henriquevital00.ManageDevices.domain.entity.Device;
import com.henriquevital00.ManageDevices.domain.entity.DeviceHistory;
import com.henriquevital00.ManageDevices.domain.enums.DeviceStateEnum;
import com.henriquevital00.ManageDevices.exception.DeviceInUseException;
import com.henriquevital00.ManageDevices.exception.ResourceNotFoundException;
import com.henriquevital00.ManageDevices.mapper.DeviceMapper;
import com.henriquevital00.ManageDevices.repository.DeviceHistoryRepository;
import com.henriquevital00.ManageDevices.repository.DeviceRepository;
import com.henriquevital00.ManageDevices.utils.Constants;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DeviceServiceImpl implements  DeviceService {
    private DeviceRepository deviceRepository;
    private DeviceHistoryRepository deviceHistoryRepository;
    private DeviceMapper deviceMapper;

    private void saveDeviceHistory(Device device, String operationType) {
        DeviceHistory history = new DeviceHistory();
        history.setDeviceId(device.getId());
        history.setName(device.getName());
        history.setBrand(device.getBrand());
        history.setState(device.getState());
        history.setOperationType(operationType);
        deviceHistoryRepository.save(history);
    }

    @Override
    public DeviceDto createDevice(DeviceCreateDto deviceCreateDto) {
        Device device = deviceMapper.toEntity(deviceCreateDto);
        device.setName(deviceCreateDto.name().toUpperCase());
        device.setBrand(deviceCreateDto.brand().toUpperCase());
        Device savedDevice = deviceRepository.save(device);
        saveDeviceHistory(savedDevice, Constants.CREATION);
        return deviceMapper.toDto(savedDevice);
    }

    @Override
    public List<DeviceDto> getAllDevices(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Device> devicePage = deviceRepository.findAll(pageable);
        return devicePage.stream()
                .map(deviceMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public DeviceDto getDeviceById(Long id) {
        Optional<Device> device = deviceRepository.findById(id);
        return device.map(deviceMapper::toDto).orElseThrow(() -> new ResourceNotFoundException("id", id.toString()));
    }

    @Override
    public List<DeviceDto> getDevicesByBrand(String brand, int page, int size) {
        String searchBrand = brand.toUpperCase();
        Pageable pageable = PageRequest.of(page, size);
        Page<Device> devices = deviceRepository.getDeviceByBrand(searchBrand, pageable);
        if (devices.isEmpty()) {
            throw new ResourceNotFoundException("brand", searchBrand);
        }

        return devices.stream()
                .map(deviceMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeviceDto> getDevicesByState(DeviceStateEnum state, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Device> devices = deviceRepository.getDeviceByState(state, pageable);
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
        saveDeviceHistory(device, Constants.UPDATE);
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
        saveDeviceHistory(device, Constants.DELETION);
    }
}
