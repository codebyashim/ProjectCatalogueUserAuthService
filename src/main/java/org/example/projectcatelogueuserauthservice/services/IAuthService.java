package org.example.projectcatelogueuserauthservice.services;

import org.antlr.v4.runtime.misc.Pair;
import org.example.projectcatelogueuserauthservice.exceptions.InvalidCredentialException;
import org.example.projectcatelogueuserauthservice.exceptions.UserAlreadyExistException;
import org.example.projectcatelogueuserauthservice.exceptions.UserNotFoundException;
import org.example.projectcatelogueuserauthservice.models.User;


public interface IAuthService {
    public User signup(String name, String email, String phone, String password)
            throws UserAlreadyExistException;

    public Pair<User, String> login(String email, String password)
            throws UserNotFoundException, InvalidCredentialException;

    boolean validateToken(String token);
}
