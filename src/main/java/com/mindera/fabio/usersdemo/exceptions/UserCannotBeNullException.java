package com.mindera.fabio.usersdemo.exceptions;

public class UserCannotBeNullException extends RuntimeException{
    public UserCannotBeNullException() {
        super("User object cannot be null");
    }
}
