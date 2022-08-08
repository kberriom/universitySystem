package com.practice.universitysystem.controller;

import com.practice.universitysystem.dto.student.StudentUpdateDto;
import com.practice.universitysystem.model.users.student.Student;
import com.practice.universitysystem.service.AuthService;
import com.practice.universitysystem.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    StudentService studentService;
    @Autowired
    AuthService authService;

    @GetMapping("/getStudentInfo")
    @Secured("ROLE_STUDENT")
    public Student getStudentInfo() {
        String email = authService.getAuthUserEmail();
        return studentService.getStudent(email);
    }

    @GetMapping("/getStudentInfo/{id}")
    @Secured("ROLE_ADMIN")
    public Student getStudentInfoById(@PathVariable long id) {
        return studentService.getStudent(id);
    }

    @GetMapping("/getAllStudentInfo")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<Student>> getAllStudentInfo() {
        return new ResponseEntity<>(studentService.getAllStudents(), HttpStatus.OK);
    }

    @PatchMapping("updateStudentInfo")
    @Secured("ROLE_STUDENT")
    public ResponseEntity<Student> updateStudentInfo(@RequestBody StudentUpdateDto studentUpdateDto) {
        String email = authService.getAuthUserEmail();
        Student student = studentService.updateStudent(email, studentUpdateDto);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @PatchMapping("updateStudentInfo/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Student> updateStudentInfoById(@RequestBody StudentUpdateDto studentUpdateDto, @PathVariable long id) {
        Student student = studentService.updateStudent(id, studentUpdateDto);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @DeleteMapping("deleteStudentInfo")
    @Secured("ROLE_STUDENT")
    public ResponseEntity<Student> deleteStudentInfo() {
        String email = authService.getAuthUserEmail();
        studentService.deleteStudent(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("deleteStudentInfo/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Student> deleteStudentInfoById(@PathVariable long id) {
        studentService.deleteStudent(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
