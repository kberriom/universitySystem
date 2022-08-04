package com.practice.universitysystem.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;


@Data
@AllArgsConstructor
public class StudentDto {

    private String name;

    private String lastName;

    private String governmentId;

    private String email;

    private String mobilePhone;
    private String landPhone;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date birthdate;

    private String userPassword;

    private String username;

}