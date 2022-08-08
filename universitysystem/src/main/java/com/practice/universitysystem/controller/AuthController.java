package com.practice.universitysystem.controller;

import com.practice.universitysystem.dto.credentials.LoginCredentialsDto;
import com.practice.universitysystem.dto.credentials.NewPasswordDto;
import com.practice.universitysystem.dto.users.student.StudentDto;
import com.practice.universitysystem.dto.users.teacher.TeacherDto;
import com.practice.universitysystem.model.users.student.Student;
import com.practice.universitysystem.model.users.teacher.Teacher;
import com.practice.universitysystem.service.AuthService;
import com.practice.universitysystem.service.users.StudentService;
import com.practice.universitysystem.service.users.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private AuthService authService;

    @PostMapping("/createStudent")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Student> createStudent(@RequestBody StudentDto userDto) {

        Student student = studentService.createUser(userDto);

        return new ResponseEntity<>(student, HttpStatus.CREATED);
    }

    @PostMapping("/createTeacher")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Teacher> createTeacher(@RequestBody TeacherDto userDto) {

        Teacher teacher = teacherService.createUser(userDto);

        return new ResponseEntity<>(teacher, HttpStatus.CREATED);
    }

    @GetMapping("/login")
    public Map<String, Object> login(@RequestBody LoginCredentialsDto credentials) {
        try {
            return Collections.singletonMap("jwt-token", authService.authAndGenerateJwt(credentials.getEmail(), credentials.getPassword()));

        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid credentials");
        }
    }

    @GetMapping("/updatePassword")
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> updatePassword(@RequestBody NewPasswordDto credentials) {
        try {
            authService.usernamePasswordAuthentication(credentials.getEmail(), credentials.getOldPassword());

            authService.changePassword(credentials.getEmail(), credentials.getNewPassword());

            String token = authService.authAndGenerateJwt(credentials.getEmail(), credentials.getNewPassword());

            return Collections.singletonMap("jwt-token", token);

        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid credentials");
        }
    }


}
