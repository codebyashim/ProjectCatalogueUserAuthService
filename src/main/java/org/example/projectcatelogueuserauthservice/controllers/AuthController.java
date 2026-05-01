package org.example.projectcatelogueuserauthservice.controllers;

import org.example.projectcatelogueuserauthservice.dtos.LoginRequestDto;
import org.example.projectcatelogueuserauthservice.dtos.SignupRequestDto;
import org.example.projectcatelogueuserauthservice.dtos.UserDto;
import org.example.projectcatelogueuserauthservice.services.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IAuthService service;

    @PostMapping("/signup")
    public UserDto signup(@RequestBody SignupRequestDto request) {

        return null;
    }

    @PostMapping("/login")
    public UserDto login(@RequestBody LoginRequestDto request) {
        return null;
    }
}
