package com.practice.universitysystem.controller;

import com.practice.universitysystem.dto.LoginCredentialsDto;
import com.practice.universitysystem.dto.StudentDto;
import com.practice.universitysystem.dto.TeacherDto;
import com.practice.universitysystem.model.users.student.Student;
import com.practice.universitysystem.model.users.teacher.Teacher;
import com.practice.universitysystem.security.JwtUtil;
import com.practice.universitysystem.service.StudentService;
import com.practice.universitysystem.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;

    @PostMapping("/createStudent")
    public ResponseEntity<Student> createStudent(@RequestBody StudentDto userDto) {

        studentService.createStudent(userDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/createTeacher")
    public ResponseEntity<Teacher> createTeacher(@RequestBody TeacherDto userDto) {

        teacherService.createTeacher(userDto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/login")
    public Map<String, Object> login(@RequestBody LoginCredentialsDto credentials) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(credentials.getEmail(), credentials.getPassword());

            authManager.authenticate(authenticationToken);

            String token = jwtUtil.generateToken(credentials.getEmail());

            return Collections.singletonMap("jwt-token", token);

        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid credentials");
        }
    }
}
