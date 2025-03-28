package com.henriquevital00.ManageDevices.controller;

import com.henriquevital00.ManageDevices.domain.dto.DeviceCreateDto;
import com.henriquevital00.ManageDevices.domain.dto.DeviceDto;
import com.henriquevital00.ManageDevices.services.DeviceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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
}
