package com.practice.universitysystem.controller.users;

import com.practice.universitysystem.dto.users.TeacherDto;
import com.practice.universitysystem.model.users.teacher.Teacher;
import com.practice.universitysystem.service.AuthService;
import com.practice.universitysystem.service.users.teacher.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
        return teacherService.getUser(email);
    }

    @GetMapping("/getTeacherInfo/{id}")
    @Secured("ROLE_ADMIN")
    public Teacher getTeacherInfoById(@PathVariable long id) {
        return teacherService.getUser(id);
    }

    @GetMapping("/getAllTeacherInfo")
    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_ADMIN")
    public List<Teacher> getAllTeacherInfo() {
        return teacherService.getAllUsers();
    }

    @GetMapping("/getAllTeacherInfo/paged")
    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_ADMIN")
    public List<Object> getAllTeacherInfoPaged(@RequestParam int page, @RequestParam int size) {
        return teacherService.getUserPaginatedList(page, size);
    }

    @PatchMapping("/updateTeacherInfo")
    @Secured("ROLE_TEACHER")
    public Teacher updateTeacherInfo(@RequestBody TeacherDto teacherUpdateDto) {
        String email = authService.getAuthUserEmail();
        return teacherService.updateUser(email, teacherUpdateDto);
    }

    @PatchMapping("/updateTeacherInfo/{id}")
    @Secured("ROLE_ADMIN")
    public Teacher updateTeacherInfoById(@PathVariable long id, @RequestBody TeacherDto teacherUpdateDto) {
        return teacherService.updateUser(id, teacherUpdateDto);
    }

    @DeleteMapping("/deleteTeacherInfo")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured("ROLE_TEACHER")
    public void deleteTeacherInfo() {
        String email = authService.getAuthUserEmail();
        teacherService.deleteUser(email);
    }

    @DeleteMapping("/deleteTeacherInfo/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured("ROLE_ADMIN")
    public void deleteTeacherInfoById(@PathVariable long id) {
        teacherService.deleteUser(id);
    }

}
