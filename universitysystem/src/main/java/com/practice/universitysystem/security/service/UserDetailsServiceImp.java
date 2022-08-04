package com.practice.universitysystem.security.service;

import com.practice.universitysystem.model.users.UniversityUser;
import com.practice.universitysystem.model.users.admin.Admin;
import com.practice.universitysystem.model.users.student.Student;
import com.practice.universitysystem.model.users.teacher.Teacher;
import com.practice.universitysystem.repository.users.UniversityUserRepository;
import com.practice.universitysystem.repository.users.admin.AdminRepository;
import com.practice.universitysystem.repository.users.student.StudentRepository;
import com.practice.universitysystem.repository.users.teacher.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Component
public class UserDetailsServiceImp implements UserDetailsService {

    @Autowired
    UniversityUserRepository userRepository;
    @Autowired
    TeacherRepository teacherRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    AdminRepository adminRepository;

    @Override

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

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

        Optional<UniversityUser> findUser = userRepository.findByEmail(email);
        if (findUser.isEmpty()) {
            throw new UsernameNotFoundException("Unable to find user using email");
        }
        UniversityUser universityUser = findUser.get();

        return new User(email, universityUser.getUserPassword(), Collections.singletonList(new SimpleGrantedAuthority(role)));
    }
}
