package org.example.projectcatelogueuserauthservice.exceptions;

public class InvalidCredentialException extends RuntimeException {
    public InvalidCredentialException(String message){
        super(message);
    }
}
