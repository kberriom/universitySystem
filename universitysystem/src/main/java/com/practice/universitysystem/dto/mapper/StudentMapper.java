package com.practice.universitysystem.dto.mapper;

import com.practice.universitysystem.dto.StudentDto;
import com.practice.universitysystem.model.users.student.Student;
import org.mapstruct.Mapper;

@Mapper
public interface StudentMapper {

    StudentDto studentToDTO(Student student);

    Student dtoToStudent(StudentDto studentDto);

}
