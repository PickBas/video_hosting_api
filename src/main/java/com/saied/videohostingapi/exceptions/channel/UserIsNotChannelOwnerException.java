package com.saied.videohostingapi.exceptions.channel;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class UserIsNotChannelOwnerException extends Exception {

    public UserIsNotChannelOwnerException(String err) {
        super(err);
    }
}
