package com.henriquevital00.ManageDevices.controller.services;

import com.henriquevital00.ManageDevices.domain.dto.DeviceCreateDto;
import com.henriquevital00.ManageDevices.domain.dto.DeviceDto;
import com.henriquevital00.ManageDevices.domain.entity.Device;
import com.henriquevital00.ManageDevices.domain.enums.DeviceStateEnum;
import com.henriquevital00.ManageDevices.exception.ResourceNotFoundException;
import com.henriquevital00.ManageDevices.mapper.DeviceMapper;
import com.henriquevital00.ManageDevices.repository.DeviceRepository;
import com.henriquevital00.ManageDevices.services.DeviceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeviceServiceTest {
    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private DeviceMapper deviceMapper;

    @InjectMocks
    private DeviceServiceImpl deviceService;

    private DeviceCreateDto deviceCreateDto;
    private Device device;
    private DeviceDto deviceDto;

    @BeforeEach
    void setUp() {
        deviceCreateDto = new DeviceCreateDto("Value1", "Value2", DeviceStateEnum.AVAILABLE);
        device = new Device(1L, "Value1", "Value2", DeviceStateEnum.AVAILABLE, LocalDateTime.now());
        deviceDto = new DeviceDto(1L, "Value1", "Value2", DeviceStateEnum.AVAILABLE);
    }

    @Test
    void createDevice_ShouldReturnDeviceDto() {
        when(deviceMapper.toEntity(any(DeviceCreateDto.class))).thenReturn(device);
        when(deviceRepository.save(any(Device.class))).thenReturn(device);
        when(deviceMapper.toDto(any(Device.class))).thenReturn(deviceDto);

        DeviceDto result = deviceService.createDevice(deviceCreateDto);

        assertNotNull(result);
        assertEquals(deviceDto.id(), result.id());
        assertEquals(deviceDto.name(), result.name());
        assertEquals(deviceDto.state(), result.state());
        assertEquals(deviceDto.brand(), result.brand());
    }

    @Test
    void getAllDevices_ShouldReturnListOfDeviceDtos() {
        Device device1 = new Device(1L, "Device1", "Brand1", DeviceStateEnum.AVAILABLE, null);
        Device device2 = new Device(2L, "Device2", "Brand2", DeviceStateEnum.INACTIVE, null);
        DeviceDto deviceDto1 = new DeviceDto(1L, "Device1", "Brand1", DeviceStateEnum.AVAILABLE);
        DeviceDto deviceDto2 = new DeviceDto(2L, "Device2", "Brand2", DeviceStateEnum.INACTIVE);

        when(deviceRepository.findAll()).thenReturn(Arrays.asList(device1, device2));
        when(deviceMapper.toDto(device1)).thenReturn(deviceDto1);
        when(deviceMapper.toDto(device2)).thenReturn(deviceDto2);

        List<DeviceDto> result = deviceService.getAllDevices();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(deviceDto1, result.get(0));
        assertEquals(deviceDto2, result.get(1));
    }

    @Test
    void getAllDevices_ShouldReturnEmptyListOfDeviceDtos() {
        when(deviceRepository.findAll()).thenReturn(new ArrayList<>());

        List<DeviceDto> result = deviceService.getAllDevices();

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void getDeviceById_ShouldReturnDeviceDto() {
        long id = 1L;

        when(deviceRepository.findById(id)).thenReturn(Optional.of(device));
        when(deviceMapper.toDto(device)).thenReturn(deviceDto);

        DeviceDto response = deviceService.getDeviceById(id);

        assertNotNull(response);
        assertEquals(deviceDto, response);
    }

    @Test
    void getDeviceById_ShouldReturnNotFound() {
        long id = 1L;

        when(deviceRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = org.junit.jupiter.api.Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> deviceService.getDeviceById(id)
        );

        assertEquals("id not found with the given input data 1", exception.getMessage());
    }
}
