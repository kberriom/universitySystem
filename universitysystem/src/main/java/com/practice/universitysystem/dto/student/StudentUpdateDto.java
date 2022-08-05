package com.practice.universitysystem.dto.student;

import lombok.AllArgsConstructor;
import lombok.Data;



@Data
@AllArgsConstructor
public class StudentUpdateDto {

    private String name;

    private String lastName;

    private String mobilePhone;
    private String landPhone;

}