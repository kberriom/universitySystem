package com.practice.universitysystem.service.users.student;

import com.practice.universitysystem.dto.users.StudentDto;
import com.practice.universitysystem.model.users.student.Student;
import com.practice.universitysystem.service.users.UserMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper
public interface StudentMapper extends UserMapper<Student, StudentDto> {

    Student dtoToUser(StudentDto studentDto);

    StudentDto userToDTO(Student student);

    @BeanMapping(nullValuePropertyMappingStrategy =  NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(target = "userPassword", ignore = true)
    @Mapping(target = "id", ignore = true)
    Student adminUpdate(@MappingTarget Student student, StudentDto studentDto);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(target = "governmentId", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "birthdate", ignore = true)
    @Mapping(target = "userPassword", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "id", ignore = true)
    Student update(@MappingTarget Student student, StudentDto studentDto);
}
