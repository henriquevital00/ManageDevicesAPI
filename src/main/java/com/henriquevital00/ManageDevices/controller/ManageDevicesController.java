package com.henriquevital00.ManageDevices.controller;

import com.henriquevital00.ManageDevices.domain.dto.DeviceCreateDto;
import com.henriquevital00.ManageDevices.domain.dto.DeviceDto;
import com.henriquevital00.ManageDevices.domain.dto.ErrorResponseDto;
import com.henriquevital00.ManageDevices.domain.enums.DeviceStateEnum;
import com.henriquevital00.ManageDevices.services.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
@RequestMapping(path="/v1/devices", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
@Validated
@AllArgsConstructor
@Log4j2
public class ManageDevicesController {
    private DeviceService deviceService;

    @Operation(
            summary = "Create Device REST API",
            description = "REST API to create new Device"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "HTTP Status CREATED"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "HTTP Status Bad Request"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @PostMapping("/create")
    public ResponseEntity<DeviceDto> createDevice(@Valid @RequestBody DeviceCreateDto device) {
        DeviceDto createdDevice = deviceService.createDevice(device);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Location", "/api/devices/" + createdDevice.id())
                .body(createdDevice);
    }

    @Operation(
            summary = "Fetch All Devices REST API",
            description = "REST API to Fetch all Devices data"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<DeviceDto>> getAllDevices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<DeviceDto> devices = deviceService.getAllDevices(page, size);
        return ResponseEntity.ok(devices);
    }

    @Operation(
            summary = "Fetch Device by ID REST API",
            description = "REST API to Fetch a Device by ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "HTTP Status Not Found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<DeviceDto> getDeviceById(@PathVariable Long id) {
        DeviceDto device = deviceService.getDeviceById(id);
        return ResponseEntity.ok(device);
    }

    @Operation(
            summary = "Fetch Devices by Brand REST API",
            description = "REST API to Fetch Devices by Brand"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "HTTP Status Not Found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @GetMapping("/brand")
    public ResponseEntity<List<DeviceDto>> getDevicesByBrand(@RequestParam String brand,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size) {
        List<DeviceDto> devices = deviceService.getDevicesByBrand(brand, page, size);
        return ResponseEntity.ok(devices);
    }

    @Operation(
            summary = "Fetch Devices by State REST API",
            description = "REST API to Fetch Devices by State"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "HTTP Status Not Found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @GetMapping("/state")
    public ResponseEntity<List<DeviceDto>> getDevicesByState(@RequestParam DeviceStateEnum state,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size) {
        List<DeviceDto> devices = deviceService.getDevicesByState(state, page, size);
        return ResponseEntity.ok(devices);
    }

    @Operation(
            summary = "Update Device REST API",
            description = "REST API to Update a Device"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "HTTP Status Not Found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<DeviceDto> updateDevice(@PathVariable Long id, @Valid @RequestBody DeviceCreateDto device) {
        DeviceDto updatedDevice = deviceService.updateDevice(id, device);
        return ResponseEntity.ok(updatedDevice);
    }

    @Operation(
            summary = "Delete Device REST API",
            description = "REST API to Delete a Device"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "HTTP Status No Content"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "HTTP Status Not Found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable Long id) {
        deviceService.deleteDevice(id);
        return ResponseEntity.noContent().build();
    }
}
