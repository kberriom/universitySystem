package com.practice.universitysystem.service.users.student;

import com.practice.universitysystem.dto.users.StudentDto;
import com.practice.universitysystem.model.users.student.Student;
import com.practice.universitysystem.repository.users.UniversityUserRepository;
import com.practice.universitysystem.repository.users.student.StudentRepository;
import com.practice.universitysystem.service.AuthService;
import com.practice.universitysystem.service.users.UniversityUserService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class StudentService extends UniversityUserService<StudentDto,StudentMapper, Student, StudentRepository> {

    @Autowired
    public StudentService(AuthService authService, UniversityUserRepository userRepository, StudentRepository instanceUserRepository) {
        super(authService, Mappers.getMapper(StudentMapper.class), userRepository, instanceUserRepository);
    }
}
