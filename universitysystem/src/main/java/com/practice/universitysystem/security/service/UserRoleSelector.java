package com.practice.universitysystem.security.service;

import com.practice.universitysystem.repository.users.admin.AdminRepository;
import com.practice.universitysystem.repository.users.student.StudentRepository;
import com.practice.universitysystem.repository.users.teacher.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.practice.universitysystem.model.users.admin.Admin;
import com.practice.universitysystem.model.users.student.Student;
import com.practice.universitysystem.model.users.teacher.Teacher;

import java.util.Optional;

@Component
public class UserRoleSelector {
    @Autowired
    TeacherRepository teacherRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    AdminRepository adminRepository;

    public String getUserRoleByHierarchy(String email) {
        Optional<Teacher> teacher = teacherRepository.findByEmail(email);
        Optional<Student> student = studentRepository.findByEmail(email);
        Optional<Admin> admin = adminRepository.findByEmail(email);

        String role;

        if (admin.isPresent()) {
            role = admin.get().getRole();
        } else if (teacher.isPresent()) {
            role = teacher.get().getRole();
        } else if (student.isPresent()) {
            role = student.get().getRole();
        } else {
            role = "ROLE_USER";
        }

        return role;
    }
}
