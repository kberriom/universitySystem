package com.practice.universitysystem.service;

import com.practice.universitysystem.dto.StudentDto;
import com.practice.universitysystem.dto.mapper.StudentMapper;
import com.practice.universitysystem.model.users.student.Student;
import com.practice.universitysystem.repository.users.student.StudentRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.*;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

@Service
public class StudentService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private StudentRepository studentRepository;

    private final StudentMapper studentMapper = Mappers.getMapper(StudentMapper.class);

    public void createStudent(StudentDto studentDto) {
        String encodedPassword = passwordEncoder.encode(studentDto.getUserPassword());
        studentDto.setUserPassword(encodedPassword);

        Student student = studentMapper.dtoToStudent(studentDto);

        student.setEnrollmentDate(new Date());

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Student>> constraintViolations = validator.validate(student);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }

        studentRepository.save(student);
    }

    public void deleteStudent() {
        //todo
    }

    public void updateStudent() {
        //todo
    }

    public Student getStudent(String email) {
        Optional<Student> student = studentRepository.findByEmail(email);
        return student.orElseThrow();
    }


}
