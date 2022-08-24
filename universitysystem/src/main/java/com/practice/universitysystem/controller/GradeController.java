package com.practice.universitysystem.controller;

import com.practice.universitysystem.dto.GradeDto;
import com.practice.universitysystem.model.curriculum.subject.Grade;
import com.practice.universitysystem.model.users.student.student_subject.StudentSubjectRegistration;
import com.practice.universitysystem.service.AuthService;
import com.practice.universitysystem.service.grade.GradeService;
import com.practice.universitysystem.service.subject.SubjectService;
import com.practice.universitysystem.service.users.student.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/grade")
public class GradeController {

    @Autowired
    GradeService gradeService;
    @Autowired
    AuthService authService;
    @Autowired
    SubjectService subjectService;
    @Autowired
    StudentService studentService;

    @PutMapping("/setStudentFinalGrade")
    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_ADMIN")
    public StudentSubjectRegistration setStudentFinalGrade(
            @RequestParam Long subjectId,@RequestParam Long studentId,@RequestParam Double finalGrade) {
        return gradeService.setStudentFinalGrade(subjectId, studentId, finalGrade);
    }

    @PutMapping("/setStudentFinalGradeAuto")
    @ResponseStatus(HttpStatus.OK)
    @Secured({"ROLE_ADMIN", "ROLE_TEACHER"})
    public StudentSubjectRegistration setStudentFinalGradeAuto(@RequestParam Long subjectId,@RequestParam Long studentId) {
        return gradeService.setStudentFinalGradeAuto(subjectId, studentId);
    }

    @PostMapping("/addStudentGrade")
    @ResponseStatus(HttpStatus.CREATED)
    @Secured({"ROLE_ADMIN", "ROLE_TEACHER"})
    public StudentSubjectRegistration addStudentGrade(
            @RequestParam Long subjectId, @RequestParam Long studentId, @RequestBody GradeDto gradeDto) {
        return gradeService.addStudentGrade(subjectId, studentId, gradeDto);
    }

    @PatchMapping("/modifyStudentGrade")
    @ResponseStatus(HttpStatus.OK)
    @Secured({"ROLE_ADMIN", "ROLE_TEACHER"})
    public StudentSubjectRegistration modifyStudentGrade(
            @RequestParam Long subjectId, @RequestParam Long studentId,@RequestParam Long gradeId,@RequestBody GradeDto gradeDto) {
        return gradeService.modifyStudentGrade(subjectId, studentId, gradeId , gradeDto);
    }

    @DeleteMapping("/removeStudentGrade")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured({"ROLE_ADMIN", "ROLE_TEACHER"})
    public void removeStudentGrade(
            @RequestParam Long subjectId, @RequestParam Long studentId,@RequestParam Long gradeId) {
        gradeService.removeStudentGrade(subjectId, studentId, gradeId);
    }

    @GetMapping("/getAllStudentAllGradesInSubject")
    @ResponseStatus(HttpStatus.OK)
    @Secured({"ROLE_ADMIN", "ROLE_TEACHER"})
    public List<Set<Grade>> getAllStudentAllGradesInSubject(@RequestParam Long subjectId) {
        return gradeService.getAllStudentAllGradesInSubject(subjectId);
    }

    @GetMapping("/getAllStudentAllGrades")
    @ResponseStatus(HttpStatus.OK)
    @Secured({"ROLE_ADMIN"})
    public List<Set<Grade>> getAllStudentAllGrades() {
        return gradeService.getAllStudentAllGrades();
    }

    @GetMapping("/getOneStudentGrades")
    @ResponseStatus(HttpStatus.OK)
    @Secured({"ROLE_ADMIN", "ROLE_TEACHER"})
    public Set<Grade> getOneStudentGrades(@RequestParam Long subjectId, @RequestParam Long studentId) {
        return gradeService.getOneStudentGrades(subjectId, studentId);
    }

    @GetMapping("/getMyGrade/{subjectName}")
    @ResponseStatus(HttpStatus.OK)
    @Secured({"ROLE_STUDENT"})
    public Set<Grade> getMyGrades(@PathVariable String subjectName) {
        String email = authService.getAuthUserEmail();
        return gradeService.getOneStudentGrades(subjectService.getSubject(subjectName).getId(), studentService.getUser(email).getId());
    }

}
