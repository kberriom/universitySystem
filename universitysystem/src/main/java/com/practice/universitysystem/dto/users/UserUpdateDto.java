package com.practice.universitysystem.dto.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class UserUpdateDto {

    private String name;

    private String lastName;

    private String mobilePhone;
    private String landPhone;

}