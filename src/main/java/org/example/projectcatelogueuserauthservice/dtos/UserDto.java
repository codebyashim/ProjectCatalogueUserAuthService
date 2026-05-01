package org.example.projectcatelogueuserauthservice.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDto {
    private Long id;
    private String name;
    private String emailId;
    private List<String> roles;
}
