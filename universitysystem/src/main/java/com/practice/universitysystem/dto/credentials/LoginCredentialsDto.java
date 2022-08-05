package com.practice.universitysystem.dto.credentials;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginCredentialsDto {

    private String email;

    private String password;

}
