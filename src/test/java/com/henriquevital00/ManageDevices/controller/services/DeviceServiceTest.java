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

import static org.junit.jupiter.api.Assertions.*;
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

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> deviceService.getDeviceById(id)
        );

        assertEquals("id not found with the given input data 1", exception.getMessage());
    }

    @Test
    void getDevicesByBrand_ShouldReturnListOfDeviceDtos() {
        String brand = "Brand1";
        Device device1 = new Device(1L, "Device1", brand, DeviceStateEnum.AVAILABLE, null);
        Device device2 = new Device(2L, "Device2", brand, DeviceStateEnum.INACTIVE, null);
        DeviceDto deviceDto1 = new DeviceDto(1L, "Device1", brand, DeviceStateEnum.AVAILABLE);
        DeviceDto deviceDto2 = new DeviceDto(2L, "Device2", brand, DeviceStateEnum.INACTIVE);

        when(deviceRepository.getDeviceByBrand(brand)).thenReturn(Arrays.asList(device1, device2));
        when(deviceMapper.toDto(device1)).thenReturn(deviceDto1);
        when(deviceMapper.toDto(device2)).thenReturn(deviceDto2);

        List<DeviceDto> result = deviceService.getDevicesByBrand(brand);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(deviceDto1, result.get(0));
        assertEquals(deviceDto2, result.get(1));
    }

    @Test
    void getDevicesByBrand_ShouldThrowResourceNotFoundException() {
        String brand = "NonExistentBrand";

        when(deviceRepository.getDeviceByBrand(brand)).thenReturn(new ArrayList<>());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> deviceService.getDevicesByBrand(brand)
        );

        assertEquals("brand not found with the given input data NonExistentBrand", exception.getMessage());
    }

    @Test
    void getDevicesByState_ShouldReturnListOfDeviceDtos() {
        DeviceStateEnum state = DeviceStateEnum.AVAILABLE;
        Device device1 = new Device(1L, "Device1", "Brand1", state, null);
        Device device2 = new Device(2L, "Device2", "Brand2", state, null);
        DeviceDto deviceDto1 = new DeviceDto(1L, "Device1", "Brand1", state);
        DeviceDto deviceDto2 = new DeviceDto(2L, "Device2", "Brand2", state);

        when(deviceRepository.getDeviceByState(state)).thenReturn(Arrays.asList(device1, device2));
        when(deviceMapper.toDto(device1)).thenReturn(deviceDto1);
        when(deviceMapper.toDto(device2)).thenReturn(deviceDto2);

        List<DeviceDto> result = deviceService.getDevicesByState(state);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(deviceDto1, result.get(0));
        assertEquals(deviceDto2, result.get(1));
    }

    @Test
    void getDevicesByState_ShouldThrowResourceNotFoundException() {
        DeviceStateEnum state = DeviceStateEnum.AVAILABLE;

        when(deviceRepository.getDeviceByState(state)).thenReturn(new ArrayList<>());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> deviceService.getDevicesByState(state)
        );

        assertEquals("state not found with the given input data AVAILABLE", exception.getMessage());
    }

    @Test
    void updateDevice_ShouldUpdateDevice() {
        long id = 1L;
        DeviceCreateDto updateDto = new DeviceCreateDto("UpdatedName", "UpdatedBrand", DeviceStateEnum.AVAILABLE);
        Device updatedDevice = new Device(id, "UpdatedName", "UpdatedBrand", DeviceStateEnum.AVAILABLE, LocalDateTime.now());
        DeviceDto updatedDeviceDto = new DeviceDto(id, "UpdatedName", "UpdatedBrand", DeviceStateEnum.AVAILABLE);

        when(deviceRepository.findById(id)).thenReturn(Optional.of(device));
        when(deviceRepository.save(any(Device.class))).thenReturn(updatedDevice);
        when(deviceMapper.toDto(any(Device.class))).thenReturn(updatedDeviceDto);

        DeviceDto result = deviceService.updateDevice(id, updateDto);

        assertNotNull(result);
        assertEquals(updatedDeviceDto.id(), result.id());
        assertEquals(updatedDeviceDto.name(), result.name());
        assertEquals(updatedDeviceDto.brand(), result.brand());
        assertEquals(updatedDeviceDto.state(), result.state());
    }

    @Test
    void updateDevice_ShouldUpdateOnlyStateWhenInUse() {
        long id = 1L;
        device.setState(DeviceStateEnum.IN_USE);
        DeviceCreateDto updateDto = new DeviceCreateDto("UpdatedName", "UpdatedBrand", DeviceStateEnum.INACTIVE);
        Device updatedDevice = new Device(id, "Value1", "Value2", DeviceStateEnum.INACTIVE, LocalDateTime.now());
        DeviceDto updatedDeviceDto = new DeviceDto(id, "Value1", "Value2", DeviceStateEnum.INACTIVE);

        when(deviceRepository.findById(id)).thenReturn(Optional.of(device));
        when(deviceRepository.save(any(Device.class))).thenReturn(updatedDevice);
        when(deviceMapper.toDto(any(Device.class))).thenReturn(updatedDeviceDto);

        DeviceDto result = deviceService.updateDevice(id, updateDto);

        assertNotNull(result);
        assertEquals(updatedDeviceDto.id(), result.id());
        assertEquals(device.getName(), result.name());
        assertEquals(device.getBrand(), result.brand());
        assertEquals(updatedDeviceDto.state(), result.state());
    }

    @Test
    void updateDevice_ShouldThrowResourceNotFoundException() {
        long id = 1L;
        DeviceCreateDto updateDto = new DeviceCreateDto("UpdatedName", "UpdatedBrand", DeviceStateEnum.AVAILABLE);

        when(deviceRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> deviceService.updateDevice(id, updateDto)
        );
    }
}
