package com.mindera.fabio.usersdemo.exception_handler;

import com.mindera.fabio.usersdemo.exceptions.UserDoesNotMatchException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {

    @ExceptionHandler(UserDoesNotMatchException.class)
    public ResponseEntity<String> handleUserDoesNotMatch(UserDoesNotMatchException e){

    }
}
