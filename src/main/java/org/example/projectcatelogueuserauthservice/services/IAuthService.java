package org.example.projectcatelogueuserauthservice.services;

import org.example.projectcatelogueuserauthservice.exceptions.InvalidCredentialException;
import org.example.projectcatelogueuserauthservice.exceptions.UserAlreadyExistException;
import org.example.projectcatelogueuserauthservice.exceptions.UserNotFoundException;
import org.example.projectcatelogueuserauthservice.models.User;


public interface IAuthService {
    public User signup(String name, String emailId, String phone, String password)
            throws UserAlreadyExistException;

    public User login(String emailId, String password)
            throws UserNotFoundException, InvalidCredentialException;
}
