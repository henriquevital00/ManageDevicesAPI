package com.henriquevital00.ManageDevices.controller.services;

import com.henriquevital00.ManageDevices.domain.dto.DeviceCreateDto;
import com.henriquevital00.ManageDevices.domain.dto.DeviceDto;
import com.henriquevital00.ManageDevices.domain.entity.Device;
import com.henriquevital00.ManageDevices.domain.enums.DeviceStateEnum;
import com.henriquevital00.ManageDevices.exception.DeviceInUseException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    private int page;
    private int size;

    @BeforeEach
    void setUp() {
        page = 0;
        size = 10;
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
        Pageable pageable = PageRequest.of(page, size);
        Device device1 = new Device(1L, "Device1", "Brand1", DeviceStateEnum.AVAILABLE, null);
        Device device2 = new Device(2L, "Device2", "Brand2", DeviceStateEnum.INACTIVE, null);
        DeviceDto deviceDto1 = new DeviceDto(1L, "Device1", "Brand1", DeviceStateEnum.AVAILABLE);
        DeviceDto deviceDto2 = new DeviceDto(2L, "Device2", "Brand2", DeviceStateEnum.INACTIVE);
        Page<Device> devicePage = new PageImpl<>(Arrays.asList(device1, device2), pageable, 2);

        when(deviceRepository.findAll(pageable)).thenReturn(devicePage);
        when(deviceMapper.toDto(device1)).thenReturn(deviceDto1);
        when(deviceMapper.toDto(device2)).thenReturn(deviceDto2);

        List<DeviceDto> result = deviceService.getAllDevices(page, size);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(deviceDto1, result.get(0));
        assertEquals(deviceDto2, result.get(1));
    }

    @Test
    void getAllDevices_ShouldReturnEmptyListOfDeviceDtos() {
        Pageable pageable = PageRequest.of(page, size);
        Page<Device> devicePage = new PageImpl<>(new ArrayList<>(), pageable, 0);

        when(deviceRepository.findAll(pageable)).thenReturn(devicePage);

        List<DeviceDto> result = deviceService.getAllDevices(page, size);

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
        Pageable pageable = PageRequest.of(page, size);
        String brand = "BRAND1"; // Ensure the brand matches the actual invocation
        Device device1 = new Device(1L, "Device1", brand, DeviceStateEnum.AVAILABLE, null);
        Device device2 = new Device(2L, "Device2", brand, DeviceStateEnum.INACTIVE, null);
        DeviceDto deviceDto1 = new DeviceDto(1L, "Device1", brand, DeviceStateEnum.AVAILABLE);
        DeviceDto deviceDto2 = new DeviceDto(2L, "Device2", brand, DeviceStateEnum.INACTIVE);
        Page<Device> devicePage = new PageImpl<>(Arrays.asList(device1, device2), pageable, 2);

        when(deviceRepository.getDeviceByBrand(brand, pageable)).thenReturn(devicePage);
        when(deviceMapper.toDto(device1)).thenReturn(deviceDto1);
        when(deviceMapper.toDto(device2)).thenReturn(deviceDto2);

        List<DeviceDto> result = deviceService.getDevicesByBrand(brand, page, size);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(deviceDto1, result.get(0));
        assertEquals(deviceDto2, result.get(1));
    }

    @Test
    void getDevicesByBrand_ShouldThrowResourceNotFoundException() {
        String brand = "NONEXISTENTBRAND";

        Pageable pageable = PageRequest.of(page, size);
        Page<Device> devicePage = new PageImpl<>(new ArrayList<>(), pageable, 0);
        when(deviceRepository.getDeviceByBrand(brand, pageable)).thenReturn(devicePage);

        assertThrows(
                ResourceNotFoundException.class,
                () -> deviceService.getDevicesByBrand(brand, page, size)
        );
    }

    @Test
    void getDevicesByState_ShouldReturnListOfDeviceDtos() {
        DeviceStateEnum state = DeviceStateEnum.AVAILABLE;
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        Device device1 = new Device(1L, "Device1", "Brand1", state, null);
        Device device2 = new Device(2L, "Device2", "Brand2", state, null);
        DeviceDto deviceDto1 = new DeviceDto(1L, "Device1", "Brand1", state);
        DeviceDto deviceDto2 = new DeviceDto(2L, "Device2", "Brand2", state);
        Page<Device> devicePage = new PageImpl<>(Arrays.asList(device1, device2), pageable, 2);

        when(deviceRepository.getDeviceByState(state, pageable)).thenReturn(devicePage);
        when(deviceMapper.toDto(device1)).thenReturn(deviceDto1);
        when(deviceMapper.toDto(device2)).thenReturn(deviceDto2);

        List<DeviceDto> result = deviceService.getDevicesByState(state, page, size);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(deviceDto1, result.get(0));
        assertEquals(deviceDto2, result.get(1));
    }

    @Test
    void getDevicesByState_ShouldThrowResourceNotFoundException() {
        DeviceStateEnum state = DeviceStateEnum.AVAILABLE;

        Pageable pageable = PageRequest.of(page, size);
        Page<Device> devicePage = new PageImpl<>(new ArrayList<>(), pageable, 0);

        when(deviceRepository.getDeviceByState(state, pageable)).thenReturn(devicePage);

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> deviceService.getDevicesByState(state, page, size)
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


    @Test
    void deleteDevice_ShouldDeleteDevice() {
        long id = 1L;
        Device device = new Device(id, "Device1", "Brand1", DeviceStateEnum.AVAILABLE, LocalDateTime.now());

        when(deviceRepository.findById(id)).thenReturn(Optional.of(device));

        deviceService.deleteDevice(id);

        verify(deviceRepository, times(1)).delete(device);
    }

    @Test
    void deleteDevice_ShouldThrowResourceNotFoundException() {
        long id = 1L;

        when(deviceRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> deviceService.deleteDevice(id));
    }

    @Test
    void deleteDevice_ShouldThrowDeviceInUseException() {
        long id = 1L;
        Device device = new Device(id, "Device1", "Brand1", DeviceStateEnum.IN_USE, LocalDateTime.now());

        when(deviceRepository.findById(id)).thenReturn(Optional.of(device));

        assertThrows(DeviceInUseException.class, () -> deviceService.deleteDevice(id));
    }
}
