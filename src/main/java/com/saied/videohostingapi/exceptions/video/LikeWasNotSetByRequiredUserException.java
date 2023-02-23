package com.saied.videohostingapi.exceptions.video;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class LikeWasNotSetByRequiredUserException extends Exception {

    public LikeWasNotSetByRequiredUserException(String err) {
        super(err);
    }
}
