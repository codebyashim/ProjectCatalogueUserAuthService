package org.example.projectcatelogueuserauthservice.services;

import org.example.projectcatelogueuserauthservice.exceptions.InvalidCredentialException;
import org.example.projectcatelogueuserauthservice.exceptions.UserAlreadyExistException;
import org.example.projectcatelogueuserauthservice.exceptions.UserNotFoundException;
import org.example.projectcatelogueuserauthservice.models.User;
import org.example.projectcatelogueuserauthservice.repos.RoleRepo;
import org.example.projectcatelogueuserauthservice.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthService implements IAuthService {
//    public AuthService(UserRepo userRepo, RoleRepo roleRepo, BCryptPasswordEncoder encoder){
//        this.userRepo = userRepo;
//        this.roleRepo = roleRepo;
//        this.bCryptPasswordEncoder = encoder;
//    }
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public User signup(String name, String emailId, String phone, String password) throws UserAlreadyExistException {
        User user = userRepo.findUserByEmail(emailId).orElse(null);
        if (user != null){
            throw new UserAlreadyExistException("User already exist, Please try out different emailId");
        }

        user = new User();
        user.setEmail(emailId);
        user.setCreatedAt(new Date());
        user.setName(name);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setPhone(phone);
        //Check and assign role
        return userRepo.save(user);
    }

    @Override
    public User login(String emailId, String password)
            throws UserNotFoundException, InvalidCredentialException {
        User user = userRepo.findUserByEmail(emailId)
                .orElseThrow(() -> new UserNotFoundException("User not found, Please signup first"));
        if (!bCryptPasswordEncoder.matches(password, user.getPassword())){
            throw new InvalidCredentialException("Invalid credentials.");
        }

        //Generate token
        return null;
    }

}
