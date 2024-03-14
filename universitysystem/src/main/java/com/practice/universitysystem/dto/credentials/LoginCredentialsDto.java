package com.practice.universitysystem.dto.credentials;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginCredentialsDto {

    private String email;

    private String password;

}
