package com.practice.universitysystem.service;

import com.practice.universitysystem.dto.TeacherDto;
import com.practice.universitysystem.dto.mapper.TeacherMapper;
import com.practice.universitysystem.model.users.teacher.Teacher;
import com.practice.universitysystem.repository.users.teacher.TeacherRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.*;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

@Service
public class TeacherService {

    @Autowired
    private AuthService authService;
    @Autowired
    private TeacherRepository teacherRepository;

    private final TeacherMapper teacherMapper = Mappers.getMapper(TeacherMapper.class);

    public Teacher createTeacher(TeacherDto teacherDto) {
        String encodedPassword = authService.getEncodedPassword(teacherDto.getUserPassword());
        teacherDto.setUserPassword(encodedPassword);

        Teacher teacher = teacherMapper.dtoToTeacher(teacherDto);

        teacher.setEnrollmentDate(new Date());

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Teacher>> constraintViolations = validator.validate(teacher);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }

        return teacherRepository.save(teacher);
    }

    public void deleteTeacher() {
        //todo
    }

    public void updateTeacher() {
        //todo
    }

    public Teacher getTeacher(String email) {
        Optional<Teacher> teacher = teacherRepository.findByEmail(email);
        return teacher.orElseThrow();
    }


}
