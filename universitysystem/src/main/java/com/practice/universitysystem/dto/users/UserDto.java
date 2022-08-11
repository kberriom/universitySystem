package com.practice.universitysystem.dto.users;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class UserDto {

    private String name;

    private String lastName;

    private String governmentId;

    private String email;

    private String mobilePhone;
    private String landPhone;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private LocalDate birthdate;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String userPassword;

    private String username;

}