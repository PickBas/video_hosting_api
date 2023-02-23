package com.saied.videohostingapi.exceptions.img;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidImageFormatException extends Exception {

    public InvalidImageFormatException(String err) {
        super(err);
    }
}
