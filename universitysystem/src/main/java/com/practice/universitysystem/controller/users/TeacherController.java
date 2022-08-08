package com.practice.universitysystem.controller.users;

import com.practice.universitysystem.dto.users.teacher.TeacherUpdateDto;
import com.practice.universitysystem.model.users.teacher.Teacher;
import com.practice.universitysystem.service.AuthService;
import com.practice.universitysystem.service.users.TeacherService;
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
        return teacherService.getUser(email);
    }

    @GetMapping("/getTeacherInfo/{id}")
    @Secured("ROLE_ADMIN")
    public Teacher getTeacherInfoById(@PathVariable long id) {
        return teacherService.getUser(id);
    }

    @GetMapping("/getAllTeacherInfo")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<Teacher>> getAllTeacherInfo() {
        return new ResponseEntity<>(teacherService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/getAllTeacherInfo/paged")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<Object>> getAllTeacherInfoPaged(@RequestParam int page, @RequestParam int size) {
        List<Object> responseList = teacherService.getUserPaginatedList(page, size);
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    @PatchMapping("/updateTeacherInfo")
    @Secured("ROLE_TEACHER")
    public Teacher updateTeacherInfo(@RequestBody TeacherUpdateDto teacherUpdateDto) {
        String email = authService.getAuthUserEmail();
        return teacherService.updateUser(email, teacherUpdateDto);
    }

    @PatchMapping("/updateTeacherInfo/{id}")
    @Secured("ROLE_ADMIN")
    public Teacher updateTeacherInfoById(@PathVariable long id, @RequestBody TeacherUpdateDto teacherUpdateDto) {
        return teacherService.updateUser(id, teacherUpdateDto);
    }

    @DeleteMapping("/deleteTeacherInfo")
    @Secured("ROLE_TEACHER")
    public ResponseEntity<Teacher> deleteTeacherInfo() {
        String email = authService.getAuthUserEmail();
        teacherService.deleteUser(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/deleteTeacherInfo/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Teacher> deleteTeacherInfoById(@PathVariable long id) {
        teacherService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
