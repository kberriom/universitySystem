package com.practice.universitysystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginCredentialsDto {

    private String email;

    private String password;

}
