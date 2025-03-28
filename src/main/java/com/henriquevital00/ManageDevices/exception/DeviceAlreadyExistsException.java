package com.henriquevital00.ManageDevices.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DeviceAlreadyExistsException extends RuntimeException {

    public DeviceAlreadyExistsException(String message){
        super(message);
    }

}
