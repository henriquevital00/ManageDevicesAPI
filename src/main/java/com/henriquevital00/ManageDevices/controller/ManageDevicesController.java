package com.henriquevital00.ManageDevices.controller;

import com.henriquevital00.ManageDevices.domain.dto.DeviceCreateDto;
import com.henriquevital00.ManageDevices.domain.dto.DeviceDto;
import com.henriquevital00.ManageDevices.domain.enums.DeviceStateEnum;
import com.henriquevital00.ManageDevices.services.DeviceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(
        name = "CRUD REST APIs for Manage Devices",
        description = "CRUD REST APIs to CREATE, UPDATE, FETCH AND DELETE Device details"
)
@RestController
@RequestMapping(path="/api", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
@Validated
@AllArgsConstructor
public class ManageDevicesController {
    private DeviceService deviceService;


    @PostMapping("/createDevice")
    public ResponseEntity<DeviceDto> createDevice(@Valid @RequestBody DeviceCreateDto device) {
        DeviceDto createdDevice = deviceService.createDevice(device);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Location", "/api/devices/" + createdDevice.id())
                .body(createdDevice);
    }

    @GetMapping("/devices")
    public ResponseEntity<List<DeviceDto>> getAllDevices() {
        List<DeviceDto> devices = deviceService.getAllDevices();
        return ResponseEntity.ok(devices);
    }

    @GetMapping("/device/{id}")
    public ResponseEntity<DeviceDto> getDeviceById(@PathVariable Long id) {
        DeviceDto device = deviceService.getDeviceById(id);
        return ResponseEntity.ok(device);
    }

    @GetMapping("/devices/brand")
    public ResponseEntity<List<DeviceDto>> getDevicesByBrand(@RequestParam String brand) {
        List<DeviceDto> devices = deviceService.getDevicesByBrand(brand);
        return ResponseEntity.ok(devices);
    }

    @GetMapping("/devices/state")
    public ResponseEntity<List<DeviceDto>> getDevicesByState(@RequestParam DeviceStateEnum state) {
        List<DeviceDto> devices = deviceService.getDevicesByState(state);
        return ResponseEntity.ok(devices);
    }

    @PutMapping("/device/{id}")
    public ResponseEntity<DeviceDto> updateDevice(@PathVariable Long id, @Valid @RequestBody DeviceCreateDto device) {
        DeviceDto updatedDevice = deviceService.updateDevice(id, device);
        return ResponseEntity.ok(updatedDevice);
    }

    @DeleteMapping("/device/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable Long id) {
        deviceService.deleteDevice(id);
        return ResponseEntity.noContent().build();
    }
}
