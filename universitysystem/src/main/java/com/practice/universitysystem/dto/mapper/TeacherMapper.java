package com.practice.universitysystem.dto.mapper;

import com.practice.universitysystem.dto.TeacherDto;
import com.practice.universitysystem.model.users.teacher.Teacher;
import org.mapstruct.Mapper;

@Mapper
public interface TeacherMapper {

    TeacherDto teacherToDTO(Teacher teacher);

    Teacher dtoToTeacher(TeacherDto teacherDto);
}
