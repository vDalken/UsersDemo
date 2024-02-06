package com.mindera.fabio.usersdemo.exceptions;

public class UserDoesNotMatchException extends RuntimeException{
    public UserDoesNotMatchException(){
        super("UserId and request body id do not match");
    }
}
