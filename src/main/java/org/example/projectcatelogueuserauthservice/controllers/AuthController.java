package org.example.projectcatelogueuserauthservice.controllers;

import org.antlr.v4.runtime.misc.Pair;
import org.example.projectcatelogueuserauthservice.dtos.LoginRequestDto;
import org.example.projectcatelogueuserauthservice.dtos.SignupRequestDto;
import org.example.projectcatelogueuserauthservice.dtos.UserDto;
import org.example.projectcatelogueuserauthservice.dtos.ValidateTokenRequestDto;
import org.example.projectcatelogueuserauthservice.models.Role;
import org.example.projectcatelogueuserauthservice.models.User;
import org.example.projectcatelogueuserauthservice.services.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IAuthService service;

    @PostMapping("/signup")
    public UserDto signup(@RequestBody SignupRequestDto request) {
        User user = service.signup(request.getName(), request.getEmail(), request.getPhone(), request.getPassword());
        return from(user);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto request) {
        Pair<User, String> pair = service.login(request.getEmail(), request.getPassword());
        UserDto dto = from(pair.a);
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.set(HttpHeaders.SET_COOKIE, pair.b);
        return new ResponseEntity<>(dto, headers, HttpStatus.OK);
    }

    @PostMapping("/validateToken")
    public boolean validateToken(@RequestBody ValidateTokenRequestDto request) {
        return service.validateToken(request.getToken());
    }

    UserDto from(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        List<String> roles = user.getRoles().stream().map(Role::getValue).toList();
        userDto.setRoles(roles);
        return userDto;
    }
}
