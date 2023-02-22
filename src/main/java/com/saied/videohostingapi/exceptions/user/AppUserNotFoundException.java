package com.saied.videohostingapi.exceptions.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class AppUserNotFoundException extends Exception {

    public AppUserNotFoundException(String err) {
        super(err);
    }
}
