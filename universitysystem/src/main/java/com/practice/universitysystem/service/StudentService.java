package com.practice.universitysystem.service;

import com.practice.universitysystem.dto.student.StudentDto;
import com.practice.universitysystem.dto.mapper.StudentMapper;
import com.practice.universitysystem.dto.student.StudentUpdateDto;
import com.practice.universitysystem.model.users.student.Student;
import com.practice.universitysystem.repository.users.student.StudentRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.*;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

@Service
public class StudentService {


    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private AuthService authService;

    private final StudentMapper studentMapper = Mappers.getMapper(StudentMapper.class);

    public Student createStudent(StudentDto studentDto) {
        String encodedPassword = authService.getEncodedPassword(studentDto.getUserPassword());
        studentDto.setUserPassword(encodedPassword);

        Student student = studentMapper.dtoToStudent(studentDto);

        student.setEnrollmentDate(new Date());

        validateStudent(student);

        return studentRepository.save(student);
    }

    private void validateStudent(Student student) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Student>> constraintViolations = validator.validate(student);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }

    public void deleteStudent(String email) {
        Student student = getStudent(email);
        studentRepository.delete(student);
    }

    /**
     * Deletes user using id
     * useful when the authenticated user is not the actual user being modified
     */
    public void deleteStudent(long id) {
        Student student = getStudent(id);
        studentRepository.delete(student);
    }

    @Transactional(rollbackFor = Exception.class)
    public Student updateStudent(String email, StudentUpdateDto updateDto) {
        Student student = getStudent(email);
        studentMapper.update(student, updateDto);
        validateStudent(student);
        return studentRepository.save(student);
    }

    /**
     * Updates user using id
     * useful when the authenticated user is not the actual user being modified
     */
    @Transactional(rollbackFor = Exception.class)
    public Student updateStudent(long id, StudentUpdateDto updateDto) {
        Student student = getStudent(id);
        studentMapper.update(student, updateDto);
        validateStudent(student);
        return studentRepository.save(student);
    }

    public Student getStudent(String email) {
        Optional<Student> student = studentRepository.findByEmail(email);
        return student.orElseThrow();
    }

    public Student getStudent(long id) {
        return studentRepository.findById(id).orElseThrow();
    }

}
