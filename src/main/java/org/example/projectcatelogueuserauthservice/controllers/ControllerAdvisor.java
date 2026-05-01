package org.example.projectcatelogueuserauthservice.controllers;

import org.example.projectcatelogueuserauthservice.exceptions.InvalidCredentialException;
import org.example.projectcatelogueuserauthservice.exceptions.UserAlreadyExistException;
import org.example.projectcatelogueuserauthservice.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvisor {
    @ExceptionHandler({UserNotFoundException.class, UserAlreadyExistException.class, InvalidCredentialException.class})
    public ResponseEntity<String> handleAuthException (Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
