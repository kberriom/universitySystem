package com.practice.universitysystem.controller;

import com.practice.universitysystem.dto.teacher.TeacherUpdateDto;
import com.practice.universitysystem.model.users.teacher.Teacher;
import com.practice.universitysystem.service.AuthService;
import com.practice.universitysystem.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    TeacherService teacherService;
    @Autowired
    AuthService authService;

    @GetMapping("/getTeacherInfo")
    @Secured("ROLE_TEACHER")
    public Teacher getTeacherInfo() {
        String email = authService.getAuthUserEmail();
        return teacherService.getTeacher(email);
    }

    @GetMapping("/getTeacherInfo/{id}")
    @Secured("ROLE_ADMIN")
    public Teacher getTeacherInfoById(@PathVariable long id) {
        return teacherService.getTeacher(id);
    }

    @GetMapping("/getAllTeacherInfo")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<Teacher>> getAllTeacherInfo() {
        return new ResponseEntity<>(teacherService.getAllTeachers(), HttpStatus.OK);
    }

    @PatchMapping("/updateTeacherInfo")
    @Secured("ROLE_TEACHER")
    public Teacher updateTeacherInfo(@RequestBody TeacherUpdateDto teacherUpdateDto) {
        String email = authService.getAuthUserEmail();
        return teacherService.updateTeacher(email, teacherUpdateDto);
    }

    @PatchMapping("/updateTeacherInfo/{id}")
    @Secured("ROLE_ADMIN")
    public Teacher updateTeacherInfoById(@PathVariable long id, @RequestBody TeacherUpdateDto teacherUpdateDto) {
        return teacherService.updateTeacher(id, teacherUpdateDto);
    }

    @DeleteMapping("/deleteTeacherInfo")
    @Secured("ROLE_TEACHER")
    public ResponseEntity<Teacher> deleteTeacherInfo() {
        String email = authService.getAuthUserEmail();
        teacherService.deleteTeacher(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/deleteTeacherInfo/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Teacher> deleteTeacherInfoById(@PathVariable long id) {
        teacherService.deleteTeacher(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
