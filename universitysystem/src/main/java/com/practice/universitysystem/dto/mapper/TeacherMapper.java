package com.practice.universitysystem.dto.mapper;

import com.practice.universitysystem.dto.teacher.TeacherDto;
import com.practice.universitysystem.dto.teacher.TeacherUpdateDto;
import com.practice.universitysystem.model.users.teacher.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper
public interface TeacherMapper {

    TeacherDto teacherToDTO(Teacher teacher);

    Teacher dtoToTeacher(TeacherDto teacherDto);

    void update(@MappingTarget Teacher teacher, TeacherUpdateDto teacherUpdateDto);
}
