package com.henriquevital00.ManageDevices.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class DeviceInUseException extends RuntimeException {
    public DeviceInUseException(String message) {
        super(message);
    }
}
