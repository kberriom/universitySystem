package com.practice.universitysystem.service.users;

import com.practice.universitysystem.dto.users.teacher.TeacherDto;
import com.practice.universitysystem.dto.users.teacher.TeacherMapper;
import com.practice.universitysystem.dto.users.teacher.TeacherUpdateDto;
import com.practice.universitysystem.model.users.teacher.Teacher;
import com.practice.universitysystem.repository.users.UniversityUserRepository;
import com.practice.universitysystem.repository.users.teacher.TeacherRepository;
import com.practice.universitysystem.service.AuthService;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;


@Service
public class TeacherService extends UniversityUserService<TeacherMapper, TeacherUpdateDto, TeacherDto, Teacher, TeacherRepository> {

    public TeacherService(AuthService authService, UniversityUserRepository userRepository, TeacherRepository instanceUserRepository) {
        super(authService, Mappers.getMapper(TeacherMapper.class), userRepository, instanceUserRepository);
    }
}
