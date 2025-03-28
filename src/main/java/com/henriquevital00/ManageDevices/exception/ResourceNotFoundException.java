package com.henriquevital00.ManageDevices.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String fieldName, String fieldValue){
        super(String.format("%s not found with the given input data %s", fieldName, fieldValue));
    }
}
