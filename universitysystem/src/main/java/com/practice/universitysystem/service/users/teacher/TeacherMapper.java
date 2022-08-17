package com.practice.universitysystem.service.users.teacher;

import com.practice.universitysystem.dto.users.TeacherDto;
import com.practice.universitysystem.model.users.teacher.Teacher;
import com.practice.universitysystem.service.users.UserMapper;
import org.mapstruct.*;

@Mapper
public interface TeacherMapper extends UserMapper<Teacher, TeacherDto> {

    Teacher dtoToUser(TeacherDto teacherDto);

    TeacherDto userToDTO(Teacher teacher);

    @BeanMapping(nullValuePropertyMappingStrategy =  NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(target = "userPassword", ignore = true)
    Teacher adminUpdate(@MappingTarget Teacher teacher, TeacherDto teacherDto);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(target = "governmentId", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "birthdate", ignore = true)
    @Mapping(target = "userPassword", ignore = true)
    @Mapping(target = "username", ignore = true)
    Teacher update(@MappingTarget Teacher teacher, TeacherDto teacherDto);
}
