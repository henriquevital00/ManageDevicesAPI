package com.henriquevital00.ManageDevices.controller.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.henriquevital00.ManageDevices.controller.ManageDevicesController;
import com.henriquevital00.ManageDevices.domain.dto.DeviceCreateDto;
import com.henriquevital00.ManageDevices.domain.dto.DeviceDto;
import com.henriquevital00.ManageDevices.domain.enums.DeviceStateEnum;
import com.henriquevital00.ManageDevices.exception.DeviceInUseException;
import com.henriquevital00.ManageDevices.exception.GlobalExceptionHandler;
import com.henriquevital00.ManageDevices.exception.ResourceNotFoundException;
import com.henriquevital00.ManageDevices.services.DeviceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ManageDevicesControllerTest {

    @InjectMocks
    private ManageDevicesController manageDevicesController;
    @Mock
    private DeviceService deviceService;
    private DeviceDto deviceDto;
    private DeviceCreateDto deviceCreateDto;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private int page;
    private int size;

    @BeforeEach
    public void setup() {
        page = 0;
        size = 10;
        deviceCreateDto = new DeviceCreateDto("Value1", "Value2", DeviceStateEnum.AVAILABLE);
        deviceDto = new DeviceDto(1L, "Value1", "Value2", DeviceStateEnum.AVAILABLE);
        mockMvc = MockMvcBuilders
                .standaloneSetup(manageDevicesController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void createDeviceReturnsCreatedStatus() throws Exception {
        when(deviceService.createDevice(any(DeviceCreateDto.class))).thenReturn(deviceDto);

        ResponseEntity<DeviceDto> response = manageDevicesController.createDevice(deviceCreateDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

    }

    @Test
    void createDeviceReturnsLocationHeader() {
        when(deviceService.createDevice(any(DeviceCreateDto.class))).thenReturn(deviceDto);

        ResponseEntity<DeviceDto> response = manageDevicesController.createDevice(deviceCreateDto);

        assertTrue(response.getHeaders().containsKey("Location"));
        assertEquals("/api/devices/" + deviceDto.id(), response.getHeaders().getLocation().getPath());
    }


    @Test
    void createDeviceReturnsDeviceDto() {
        when(deviceService.createDevice(any(DeviceCreateDto.class))).thenReturn(deviceDto);

        ResponseEntity<DeviceDto> response = manageDevicesController.createDevice(deviceCreateDto);

        assertNotNull(response.getBody());
        assertEquals(deviceDto.id(), response.getBody().id());
        assertEquals(deviceDto.name(), response.getBody().name());
        assertEquals(deviceDto.state(), response.getBody().state());
        assertEquals(deviceDto.brand(), response.getBody().brand());
    }

    @Test
    void createDeviceWithNullStateReturnsBadRequest() throws Exception {
        String url = "/api/createDevice";
        DeviceCreateDto invalidDeviceDto = new DeviceCreateDto("Value1", "Value2", null);

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDeviceDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllDevices_ShouldReturnListOfDeviceDtos() {
        DeviceDto deviceDto1 = new DeviceDto(1L, "Device1", "Brand1", DeviceStateEnum.AVAILABLE);
        DeviceDto deviceDto2 = new DeviceDto(2L, "Device2", "Brand2", DeviceStateEnum.INACTIVE);

        when(deviceService.getAllDevices(page, size)).thenReturn(Arrays.asList(deviceDto1, deviceDto2));

        ResponseEntity<List<DeviceDto>> response = manageDevicesController.getAllDevices(page, size);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(deviceDto1, response.getBody().get(0));
        assertEquals(deviceDto2, response.getBody().get(1));
    }

    @Test
    void getAllDevices_ShouldReturnOkStatus() throws Exception {
        int page = 0;
        int size = 10;

        when(deviceService.getAllDevices(page, size)).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/devices")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk());
    }

    @Test
    void getDeviceById_ShouldReturnDeviceDto() {
        when(deviceService.getDeviceById(1L)).thenReturn(deviceDto);

        ResponseEntity<DeviceDto> response = manageDevicesController.getDeviceById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(deviceDto, response.getBody());
    }

    @Test
    void getDeviceById_ShouldReturnNotFoundStatus() throws Exception {
        when(deviceService.getDeviceById(1L)).thenThrow(new ResourceNotFoundException("id", "1"));

        mockMvc.perform(get("/api/device/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getDevicesByBrand_ShouldReturnListOfDeviceDtos() throws Exception {
        String brand = "Brand1";
        DeviceDto deviceDto1 = new DeviceDto(1L, "Device1", brand, DeviceStateEnum.AVAILABLE);
        DeviceDto deviceDto2 = new DeviceDto(2L, "Device2", brand, DeviceStateEnum.INACTIVE);

        when(deviceService.getDevicesByBrand(brand, page, size)).thenReturn(Arrays.asList(deviceDto1, deviceDto2));

        mockMvc.perform(get("/api/devices/brand")
                        .param("brand", brand)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(deviceDto1.id()))
                .andExpect(jsonPath("$[0].name").value(deviceDto1.name()))
                .andExpect(jsonPath("$[0].brand").value(deviceDto1.brand()))
                .andExpect(jsonPath("$[0].state").value(deviceDto1.state().toString()))
                .andExpect(jsonPath("$[1].id").value(deviceDto2.id()))
                .andExpect(jsonPath("$[1].name").value(deviceDto2.name()))
                .andExpect(jsonPath("$[1].brand").value(deviceDto2.brand()))
                .andExpect(jsonPath("$[1].state").value(deviceDto2.state().toString()));
    }

    @Test
    void getDevicesByBrand_ShouldThrowResourceNotFoundException() throws Exception {
        String brand = "NonExistentBrand";

        when(deviceService.getDevicesByBrand(brand, page, size)).thenThrow(new ResourceNotFoundException("brand", brand));

        mockMvc.perform(get("/api/devices/brand")
                        .param("brand", brand)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getDevicesByState_ShouldReturnListOfDeviceDtos() throws Exception {
        DeviceStateEnum state = DeviceStateEnum.AVAILABLE;
        DeviceDto deviceDto1 = new DeviceDto(1L, "Device1", "Brand1", state);
        DeviceDto deviceDto2 = new DeviceDto(2L, "Device2", "Brand2", state);

        when(deviceService.getDevicesByState(state, 0, 10)).thenReturn(Arrays.asList(deviceDto1, deviceDto2));

        mockMvc.perform(get("/api/devices/state")
                        .param("state", state.toString())
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(deviceDto1.id()))
                .andExpect(jsonPath("$[0].name").value(deviceDto1.name()))
                .andExpect(jsonPath("$[0].brand").value(deviceDto1.brand()))
                .andExpect(jsonPath("$[0].state").value(deviceDto1.state().toString()))
                .andExpect(jsonPath("$[1].id").value(deviceDto2.id()))
                .andExpect(jsonPath("$[1].name").value(deviceDto2.name()))
                .andExpect(jsonPath("$[1].brand").value(deviceDto2.brand()))
                .andExpect(jsonPath("$[1].state").value(deviceDto2.state().toString()));
    }

    @Test
    void getDevicesByState_ShouldThrowResourceNotFoundException() throws Exception {
        DeviceStateEnum state = DeviceStateEnum.AVAILABLE;

        when(deviceService.getDevicesByState(state, 0, 10)).thenThrow(new ResourceNotFoundException("state", state.toString()));

        mockMvc.perform(get("/api/devices/state")
                        .param("state", state.toString())
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateDevice_ShouldUpdateDevice() {
        long id = 1L;
        DeviceCreateDto updateDto = new DeviceCreateDto("UpdatedName", "UpdatedBrand", DeviceStateEnum.AVAILABLE);
        DeviceDto updatedDeviceDto = new DeviceDto(id, "UpdatedName", "UpdatedBrand", DeviceStateEnum.AVAILABLE);

        when(deviceService.updateDevice(id, updateDto)).thenReturn(updatedDeviceDto);

        ResponseEntity<DeviceDto> result = manageDevicesController.updateDevice(id, updateDto);

        DeviceDto responseBody = result.getBody();

        assertNotNull(result.getBody());
        assertEquals(result.getStatusCode(), HttpStatus.OK);
        assertEquals(updatedDeviceDto.id(), responseBody.id());
        assertEquals(updatedDeviceDto.name(), responseBody.name());
        assertEquals(updatedDeviceDto.brand(), responseBody.brand());
        assertEquals(updatedDeviceDto.state(), responseBody.state());
    }

    @Test
    void deleteDevice_ShouldReturnNoContentStatus() throws Exception {
        long id = 1L;

        mockMvc.perform(delete("/api/device/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteDevice_ShouldReturnNotFoundStatus() throws Exception {
        long id = 1L;

        doThrow(new ResourceNotFoundException("id", String.valueOf(id))).when(deviceService).deleteDevice(id);

        mockMvc.perform(delete("/api/device/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteDevice_ShouldReturnConflictStatusWhenDeviceInUse() throws Exception {
        long id = 1L;

        doThrow(new DeviceInUseException("Device is currently in use")).when(deviceService).deleteDevice(id);

        mockMvc.perform(delete("/api/device/{id}", id))
                .andExpect(status().isForbidden());
    }
}
