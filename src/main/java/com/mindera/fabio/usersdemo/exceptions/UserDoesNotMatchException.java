package com.mindera.fabio.usersdemo.exceptions;

public class UserDoesNotMatchException extends RuntimeException{
    public UserDoesNotMatchException(){
        super("User's object doesn't match path variable id");
    }
}
