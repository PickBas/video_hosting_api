package com.saied.videohostingapi.exceptions.channel;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserIsAlreadySubscribedException extends Exception {

    public UserIsAlreadySubscribedException(String err) {
        super(err);
    }
}
