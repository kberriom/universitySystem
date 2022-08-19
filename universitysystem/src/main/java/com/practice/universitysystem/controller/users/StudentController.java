package com.practice.universitysystem.controller.users;

import com.practice.universitysystem.dto.users.StudentDto;
import com.practice.universitysystem.model.users.student.Student;
import com.practice.universitysystem.service.AuthService;
import com.practice.universitysystem.service.users.student.StudentService;
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
@RequestMapping("/student")
public class StudentController {

    @Autowired
    StudentService studentService;
    @Autowired
    AuthService authService;

    @GetMapping("/getStudentInfo")
    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_STUDENT")
    public Student getStudentInfo() {
        String email = authService.getAuthUserEmail();
        return studentService.getUser(email);
    }

    @GetMapping("/getStudentInfo/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_ADMIN")
    public Student getStudentInfoById(@PathVariable long id) {
        return studentService.getUser(id);
    }

    @GetMapping("/getAllStudentInfo")
    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_ADMIN")
    public List<Student> getAllStudentInfo() {
        return studentService.getAllUsers();
    }

    @GetMapping("/getAllStudentInfo/paged")
    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_ADMIN")
    public List<Object> getAllStudentInfoPaged(@RequestParam int page, @RequestParam int size) {
        return studentService.getUserPaginatedList(page, size);
    }

    @PatchMapping("updateStudentInfo")
    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_STUDENT")
    public Student updateStudentInfo(@RequestBody StudentDto studentUpdateDto) {
        String email = authService.getAuthUserEmail();
        return studentService.updateUser(email, studentUpdateDto);
    }

    @PatchMapping("updateStudentInfo/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_ADMIN")
    public Student updateStudentInfoById(@RequestBody StudentDto studentUpdateDto, @PathVariable long id) {
        return studentService.updateUser(id, studentUpdateDto);
    }

    @DeleteMapping("deleteStudentInfo")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured("ROLE_STUDENT")
    public void deleteStudentInfo() {
        String email = authService.getAuthUserEmail();
        studentService.deleteUser(email);
    }

    @DeleteMapping("deleteStudentInfo/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured("ROLE_ADMIN")
    public void deleteStudentInfoById(@PathVariable long id) {
        studentService.deleteUser(id);
    }

}
