package com.practice.universitysystem.dto.users.teacher;

import com.practice.universitysystem.dto.users.UserMapper;
import com.practice.universitysystem.model.users.teacher.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper
public interface TeacherMapper extends UserMapper<Teacher, TeacherUpdateDto, TeacherDto> {

    TeacherDto teacherToDTO(Teacher teacher);

    Teacher dtoToTeacher(TeacherDto teacherDto);

    void update(@MappingTarget Teacher teacher, TeacherUpdateDto teacherUpdateDto);
}
