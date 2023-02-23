package com.saied.videohostingapi.exceptions.video;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CommentNotFoundException extends Exception {

    public CommentNotFoundException(String err) {
        super(err);
    }
}
