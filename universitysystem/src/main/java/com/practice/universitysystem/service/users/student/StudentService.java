package com.practice.universitysystem.service.users.student;

import com.practice.universitysystem.dto.users.StudentDto;
import com.practice.universitysystem.model.users.student.Student;
import com.practice.universitysystem.repository.users.UniversityUserRepository;
import com.practice.universitysystem.repository.users.student.StudentRepository;
import com.practice.universitysystem.repository.users.student.student_subject.StudentSubjectRegistrationRepository;
import com.practice.universitysystem.service.AuthService;
import com.practice.universitysystem.service.ServiceUtils;
import com.practice.universitysystem.service.users.UniversityUserService;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class StudentService extends UniversityUserService<StudentDto,StudentMapper, Student, StudentRepository> {

    StudentSubjectRegistrationRepository registrationRepository;

    @Autowired
    public StudentService(AuthService authService, UniversityUserRepository userRepository, StudentRepository instanceUserRepository, StudentSubjectRegistrationRepository registrationRepository) {
        super(authService, Mappers.getMapper(StudentMapper.class), userRepository, instanceUserRepository, new ServiceUtils<>(instanceUserRepository));
        this.registrationRepository = registrationRepository;
    }

    @Override
    public void deleteUser(String email) {
        if (!registrationRepository.findAllByStudentUserId(super.getUser(email).getId()).isEmpty()) {
            throw new IllegalStateException("Cannot delete Student if still has any registration");
        }
        super.deleteUser(email);
    }

    @Override
    public void deleteUser(long id) {
        if (!registrationRepository.findAllByStudentUserId(id).isEmpty()) {
            throw new IllegalStateException("Cannot delete Student if still has any registration");
        }
        super.deleteUser(id);
    }
}
