package com.practice.universitysystem.controller;

import com.practice.universitysystem.dto.SubjectDto;
import com.practice.universitysystem.model.curriculum.subject.Subject;
import com.practice.universitysystem.model.users.student.student_subject.StudentSubjectRegistration;
import com.practice.universitysystem.model.users.teacher.teacher_asignation.TeacherAssignation;
import com.practice.universitysystem.service.subject.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subject")
public class SubjectController {

    @Autowired
    SubjectService subjectService;

    @PostMapping("/createSubject")
    @ResponseStatus(HttpStatus.CREATED)
    @Secured("ROLE_ADMIN")
    public Subject createSubject(@RequestBody SubjectDto subjectDto) {
        return subjectService.createSubject(subjectDto);
    }

    @GetMapping("/getSubject")
    @ResponseStatus(HttpStatus.OK)
    public Subject getSubject(@RequestParam String name) {
        return subjectService.getSubject(name);
    }

    @GetMapping("/getAllSubjects")
    @ResponseStatus(HttpStatus.OK)
    public List<Subject> getAllSubjects() {
        return subjectService.getAllSubjects();
    }

    @GetMapping("/getAllSubjects/paged")
    @ResponseStatus(HttpStatus.OK)
    public List<Object> getAllSubjectsPaged(@RequestParam int page, @RequestParam int size) {
        return subjectService.getPaginatedSubjects(page, size);
    }

    @PatchMapping("/updateSubject/{name}")
    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_ADMIN")
    public Subject updateSubject(@PathVariable String name, @RequestBody SubjectDto subjectDto) {
        return subjectService.updateSubject(name, subjectDto);
    }

    @DeleteMapping("/deleteSubject")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured("ROLE_ADMIN")
    public void deleteSubject(@RequestParam String name) {
        subjectService.deleteSubject(name);
    }

    @GetMapping("/getAllRegisteredStudents/{subjectName}")
    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_ADMIN")
    public List<StudentSubjectRegistration> getAllRegisteredStudents(@PathVariable String subjectName) {
        return subjectService.getAllRegisteredStudents(subjectName);
    }

    @PostMapping("/addStudent")
    @ResponseStatus(HttpStatus.CREATED)
    @Secured("ROLE_ADMIN")
    public StudentSubjectRegistration addStudent(@RequestParam Long studentId, @RequestParam String subjectName) {
        return subjectService.addStudentToSubject(studentId, subjectName);
    }

    @DeleteMapping("/removeStudent")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured("ROLE_ADMIN")
    public void removeStudent(@RequestParam Long studentId, @RequestParam String subjectName) {
        subjectService.removeStudent(studentId, subjectName);
    }

    @GetMapping("/getAllTeachers/{subjectName}")
    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_ADMIN")
    public List<TeacherAssignation> getAllTeachers(@PathVariable String subjectName) {
        return subjectService.getAllTeachers(subjectName);
    }

    @PostMapping("/addTeacher")
    @ResponseStatus(HttpStatus.CREATED)
    @Secured("ROLE_ADMIN")
    public TeacherAssignation addTeacherToSubject(@RequestParam Long teacherId, @RequestParam String subjectName, @RequestParam String roleInClass) {
        return subjectService.addTeacherToSubject(teacherId, subjectName, roleInClass);
    }

    @DeleteMapping("/removeTeacher")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured("ROLE_ADMIN")
    public void removeTeacher(@RequestParam Long teacherId, @RequestParam String subjectName) {
        subjectService.removeTeacher(teacherId, subjectName);
    }

    @PostMapping("/modifyTeacherRole")
    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_ADMIN")
    public TeacherAssignation modifyTeacherRoleInSubject(@RequestParam Long teacherId, @RequestParam String subjectName, @RequestParam String roleInClass) {
        return subjectService.modifyTeacherRoleInSubject(teacherId, subjectName, roleInClass);
    }

}
