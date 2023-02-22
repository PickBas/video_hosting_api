package com.saied.videohostingapi.exceptions.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class AppUserAlreadyExistsException extends Exception {

    public AppUserAlreadyExistsException(String err) {
        super(err);
    }
}
