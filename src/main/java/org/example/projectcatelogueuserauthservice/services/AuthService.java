package org.example.projectcatelogueuserauthservice.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.antlr.v4.runtime.misc.Pair;
import org.example.projectcatelogueuserauthservice.exceptions.InvalidCredentialException;
import org.example.projectcatelogueuserauthservice.exceptions.UserAlreadyExistException;
import org.example.projectcatelogueuserauthservice.exceptions.UserNotFoundException;
import org.example.projectcatelogueuserauthservice.models.Role;
import org.example.projectcatelogueuserauthservice.models.Session;
import org.example.projectcatelogueuserauthservice.models.State;
import org.example.projectcatelogueuserauthservice.models.User;
import org.example.projectcatelogueuserauthservice.repos.RoleRepo;
import org.example.projectcatelogueuserauthservice.repos.SessionRepo;
import org.example.projectcatelogueuserauthservice.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;

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
    private SessionRepo sessionRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private SecretKey secretKey;

    @Override
    public User signup(String name, String email, String phone, String password) throws UserAlreadyExistException {
        User user = userRepo.findUserByEmail(email).orElse(null);
        if (user != null){
            throw new UserAlreadyExistException("User already exist, Please try out different email");
        }

        user = new User();
        user.setEmail(email);
        user.setCreatedAt(new Date());
        user.setName(name);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setPhone(phone);
        //Check and assign role
        Role role = roleRepo.findRoleByValue("NON_ADMIN").orElse(null);

        if (role == null){
            role = new Role();
            role.setState(State.ACTIVE);
            role.setCreatedAt(new Date());
            role.setValue("NON_ADMIN");
            roleRepo.save(role);
        }

        List<Role> roles = new ArrayList<>();
        roles.add(role);
        user.setRoles(roles);

        return userRepo.save(user);
    }

    @Override
    public Pair<User, String> login(String email, String password)
            throws UserNotFoundException, InvalidCredentialException {
        User user = userRepo.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found, Please signup first"));
        if (!bCryptPasswordEncoder.matches(password, user.getPassword())){
            throw new InvalidCredentialException("Invalid credentials.");
        }

        //Generate token
        HashMap<String, Object> payload = new HashMap<>();
        payload.put("userId", user.getId());
        List<String> roleStrings = new ArrayList<>();
        for(Role role : user.getRoles()) {
            roleStrings.add(role.getValue());
        }
        payload.put("permissions",roleStrings);
        long currentTime = System.currentTimeMillis();
        payload.put("iat", currentTime);
        payload.put("exp", currentTime+100000);
        payload.put("issued_by", "milrock");

        String token = Jwts.builder().claims(payload).signWith(secretKey).compact();

        Session session = new Session();
        session.setToken(token);
        session.setUser(user);
        session.setState(State.ACTIVE);
        session.setCreatedAt(new Date());
        sessionRepo.save(session);

        return new Pair<>(user, token);
    }

    @Override
    public boolean validateToken(String token) {
        Session session = sessionRepo.findByToken(token).orElse(null);
        if (session != null){
            JwtParser parser = Jwts.parser().verifyWith(secretKey).build();
            Claims claims = parser.parseSignedClaims(token).getPayload();
            long expiry = (long) claims.get("exp");
            long currentTime = System.currentTimeMillis();
            if (currentTime > expiry){
                session.setState(State.INACTIVE);
                sessionRepo.save(session);
                return false;
            }
            return true;
        }
        return false;
    }

}
