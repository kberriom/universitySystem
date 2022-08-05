package com.practice.universitysystem.dto.mapper;

import com.practice.universitysystem.dto.student.StudentDto;
import com.practice.universitysystem.dto.student.StudentUpdateDto;
import com.practice.universitysystem.model.users.student.Student;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper
public interface StudentMapper {

    StudentDto studentToDTO(Student student);

    Student dtoToStudent(StudentDto studentDto);

    void update(@MappingTarget Student student, StudentUpdateDto studentUpdateDto);

}
