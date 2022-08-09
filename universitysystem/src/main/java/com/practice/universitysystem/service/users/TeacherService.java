package com.practice.universitysystem.service.users;

import com.practice.universitysystem.dto.users.teacher.TeacherDto;
import com.practice.universitysystem.model.users.teacher.Teacher;
import com.practice.universitysystem.repository.users.UniversityUserRepository;
import com.practice.universitysystem.repository.users.teacher.TeacherRepository;
import com.practice.universitysystem.service.AuthService;
import org.springframework.stereotype.Service;


@Service
public class TeacherService extends UniversityUserService<TeacherDto, Teacher, TeacherRepository> {

    TeacherRepository teacherRepository;

    public TeacherService(AuthService authService, UniversityUserRepository userRepository, TeacherRepository instanceUserRepository) {
        super(authService, userRepository, instanceUserRepository);
        teacherRepository = instanceUserRepository;
    }
}
