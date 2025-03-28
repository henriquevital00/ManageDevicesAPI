package com.henriquevital00.ManageDevices.mapper;

import com.henriquevital00.ManageDevices.domain.dto.DeviceCreateDto;
import com.henriquevital00.ManageDevices.domain.dto.DeviceDto;
import com.henriquevital00.ManageDevices.domain.entity.Device;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DeviceMapper {
    DeviceDto toDto(Device device);
    Device toEntity(DeviceDto deviceDto);
    Device toEntity(DeviceCreateDto deviceDto);
}
