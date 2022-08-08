package com.practice.universitysystem.dto.users.student;

import com.practice.universitysystem.dto.users.UserMapper;
import com.practice.universitysystem.model.users.student.Student;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper
public interface StudentMapper extends UserMapper<Student, StudentUpdateDto, StudentDto> {

    StudentDto studentToDTO(Student student);

    Student dtoToStudent(StudentDto studentDto);

    void update(@MappingTarget Student student, StudentUpdateDto studentUpdateDto);

}
