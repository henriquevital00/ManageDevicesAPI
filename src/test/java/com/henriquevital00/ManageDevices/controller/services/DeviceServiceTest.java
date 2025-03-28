package com.henriquevital00.ManageDevices.controller.services;

import com.henriquevital00.ManageDevices.domain.dto.DeviceCreateDto;
import com.henriquevital00.ManageDevices.domain.dto.DeviceDto;
import com.henriquevital00.ManageDevices.domain.entity.Device;
import com.henriquevital00.ManageDevices.domain.enums.DeviceStateEnum;
import com.henriquevital00.ManageDevices.mapper.DeviceMapper;
import com.henriquevital00.ManageDevices.repository.DeviceRepository;
import com.henriquevital00.ManageDevices.services.DeviceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

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
}
