package com.mindera.fabio.usersdemo.exceptions;

public class UserFieldsCannotBeNullOrEmptyException extends RuntimeException{
    public UserFieldsCannotBeNullOrEmptyException() {
        super("User fields cannot be null or empty");
    }
}
