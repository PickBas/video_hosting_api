package com.saied.videohostingapi.exceptions.video;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CommentWasNotLeftByRequiredUserException extends Exception {

    public CommentWasNotLeftByRequiredUserException(String err) {
        super(err);
    }
}
