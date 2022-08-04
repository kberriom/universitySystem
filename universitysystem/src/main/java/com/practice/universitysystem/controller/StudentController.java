package com.practice.universitysystem.controller;

import com.practice.universitysystem.model.users.student.Student;
import com.practice.universitysystem.repository.users.student.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    StudentRepository studentRepository;

    @GetMapping("/getStudentInfo")
    @Secured("ROLE_USER")
    public Student getStudentInfo() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return studentRepository.findByEmail(email).orElseThrow();
    }
}
