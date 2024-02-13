package com.mindera.fabio.usersdemo.exception_handler;

import com.mindera.fabio.usersdemo.exceptions.UserDoesNotMatchException;
import com.mindera.fabio.usersdemo.exceptions.UserFieldsCannotBeNullOrEmptyException;
import com.mindera.fabio.usersdemo.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(UserDoesNotMatchException.class)
    public ResponseEntity<String> handleUserDoesNotMatchException(UserDoesNotMatchException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(UserFieldsCannotBeNullOrEmptyException.class)
    public ResponseEntity<String> handleUserFieldsCannotBeNullOrEmptyException(UserFieldsCannotBeNullOrEmptyException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
