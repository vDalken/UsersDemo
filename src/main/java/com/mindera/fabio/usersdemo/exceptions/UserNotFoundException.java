package com.mindera.fabio.usersdemo.exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(){
        super("User wasn't found in the database");
    }
}
