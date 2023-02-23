package com.saied.videohostingapi.exceptions.profile;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ProfileNotFoundException extends Exception {

    public ProfileNotFoundException(String err) {
        super(err);
    }
}
