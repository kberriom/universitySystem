package com.practice.universitysystem.service.users.teacher;

import com.practice.universitysystem.dto.users.TeacherDto;
import com.practice.universitysystem.model.users.teacher.Teacher;
import com.practice.universitysystem.repository.users.UniversityUserRepository;
import com.practice.universitysystem.repository.users.teacher.TeacherRepository;
import com.practice.universitysystem.repository.users.teacher.teacher_assignation.TeacherAssignationRepository;
import com.practice.universitysystem.service.AuthService;
import com.practice.universitysystem.service.ServiceUtils;
import com.practice.universitysystem.service.users.UniversityUserService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TeacherService extends UniversityUserService<TeacherDto,TeacherMapper, Teacher, TeacherRepository> {

    TeacherAssignationRepository assignationRepository;

    @Autowired
    public TeacherService(AuthService authService, UniversityUserRepository userRepository, TeacherRepository instanceUserRepository, TeacherAssignationRepository assignationRepository) {
        super(authService, Mappers.getMapper(TeacherMapper.class), userRepository, instanceUserRepository, new ServiceUtils<>(instanceUserRepository));
        this.assignationRepository = assignationRepository;
    }

    @Override
    public void deleteUser(String email) {
        if (!assignationRepository.findAllByTeacherUserId(super.getUser(email).getId()).isEmpty()) {
            throw new IllegalStateException("Cannot delete teacher if still has assigned subjects");
        }
        super.deleteUser(email);
    }

    @Override
    public void deleteUser(long id) {
        if (!assignationRepository.findAllByTeacherUserId(id).isEmpty()) {
            throw new IllegalStateException("Cannot delete teacher if still has assigned subjects");
        }
        super.deleteUser(id);
    }
}
