package com.practice.universitysystem.service.users.teacher;

import com.practice.universitysystem.dto.users.TeacherDto;
import com.practice.universitysystem.model.users.teacher.Teacher;
import com.practice.universitysystem.repository.users.UniversityUserRepository;
import com.practice.universitysystem.repository.users.teacher.TeacherRepository;
import com.practice.universitysystem.service.AuthService;
import com.practice.universitysystem.service.users.UniversityUserService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TeacherService extends UniversityUserService<TeacherDto,TeacherMapper, Teacher, TeacherRepository> {

    @Autowired
    public TeacherService(AuthService authService, UniversityUserRepository userRepository, TeacherRepository instanceUserRepository) {
        super(authService, Mappers.getMapper(TeacherMapper.class), userRepository, instanceUserRepository);
    }
}
