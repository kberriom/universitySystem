package com.practice.universitysystem.dto.credentials;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewPasswordDto {

    private String email;

    private String oldPassword;

    private String newPassword;

}
