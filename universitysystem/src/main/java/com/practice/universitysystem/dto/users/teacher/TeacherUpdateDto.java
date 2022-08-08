package com.practice.universitysystem.dto.users.teacher;

import com.practice.universitysystem.dto.users.UserUpdateDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class TeacherUpdateDto extends UserUpdateDto {

    private String department;
}
