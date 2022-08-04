package com.practice.universitysystem.controller;

import com.practice.universitysystem.dto.LoginCredentialsDto;
import com.practice.universitysystem.dto.StudentDto;
import com.practice.universitysystem.dto.mapper.StudentMapper;
import com.practice.universitysystem.model.users.student.Student;
import com.practice.universitysystem.repository.users.student.StudentRepository;
import com.practice.universitysystem.security.JwtUtil;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private StudentRepository studentRepository;

    private final StudentMapper studentMapper = Mappers.getMapper(StudentMapper.class);

    @PostMapping("/registerStudent")
    public Map<String, Object> studentRegistration(@RequestBody StudentDto userDto) {
        String encodedPassword = passwordEncoder.encode(userDto.getUserPassword());
        userDto.setUserPassword(encodedPassword);

        Student student = studentMapper.dtoToStudent(userDto);
        student.setEnrollmentDate(new Date());

        student = studentRepository.save(student);

        String token = jwtUtil.generateToken(student.getEmail());

        return Collections.singletonMap("jwt-token", token);

    }

    @PostMapping("/login")
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
