package com.saied.videohostingapi.exceptions.img;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ImageFileIsEmptyException extends Exception {

    public ImageFileIsEmptyException(String err) {
        super(err);
    }
}
