package com.practice.universitysystem.dto.users;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


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
    private Date birthdate;

    private String username;

}