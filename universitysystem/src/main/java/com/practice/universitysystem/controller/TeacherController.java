package com.practice.universitysystem.controller;

import com.practice.universitysystem.model.users.teacher.Teacher;
import com.practice.universitysystem.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    TeacherService teacherService;

    @GetMapping("/getTeacherInfo")
    @Secured("ROLE_TEACHER")
    public Teacher getTeacherInfo() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return teacherService.getTeacher(email);
    }

}
