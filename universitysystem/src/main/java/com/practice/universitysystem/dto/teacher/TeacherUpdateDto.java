package com.practice.universitysystem.dto.teacher;

import lombok.Data;

@Data
public class TeacherUpdateDto {
    private String name;

    private String lastName;

    private String mobilePhone;
    private String landPhone;

    private String department;
}
