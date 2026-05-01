package org.example.projectcatelogueuserauthservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {
    private String name;
    private String emailId;
    private String phone;
    private String password;
}
