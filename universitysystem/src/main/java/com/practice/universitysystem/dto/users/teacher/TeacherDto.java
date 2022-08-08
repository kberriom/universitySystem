package com.practice.universitysystem.dto.users.teacher;

import com.practice.universitysystem.dto.users.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TeacherDto extends UserDto {

    private String department;
}
