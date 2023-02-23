package com.saied.videohostingapi.exceptions.channel;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ChannelNotFoundException extends Exception {

    public ChannelNotFoundException (String err) {
        super(err);
    }
}
