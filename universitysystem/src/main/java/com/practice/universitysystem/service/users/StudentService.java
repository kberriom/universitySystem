package com.practice.universitysystem.service.users;

import com.practice.universitysystem.dto.users.student.StudentDto;
import com.practice.universitysystem.model.users.student.Student;
import com.practice.universitysystem.repository.users.UniversityUserRepository;
import com.practice.universitysystem.repository.users.student.StudentRepository;
import com.practice.universitysystem.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class StudentService extends UniversityUserService<StudentDto, Student, StudentRepository> {

    @Autowired
    public StudentService(AuthService authService, UniversityUserRepository userRepository, StudentRepository instanceUserRepository) {
        super(authService, userRepository, instanceUserRepository);
    }

}
