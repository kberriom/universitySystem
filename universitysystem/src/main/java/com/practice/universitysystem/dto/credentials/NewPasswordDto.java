package com.practice.universitysystem.dto.credentials;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewPasswordDto {

    private String email;

    private String oldPassword;

    private String newPassword;

}
