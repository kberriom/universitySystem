package com.practice.universitysystem.dto.users.teacher;

import com.practice.universitysystem.dto.users.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class TeacherDto extends UserDto {

    private String department;
}
