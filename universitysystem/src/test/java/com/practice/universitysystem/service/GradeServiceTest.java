package com.practice.universitysystem.service;

import com.practice.universitysystem.model.curriculum.subject.Grade;
import com.practice.universitysystem.model.users.student.student_subject.StudentSubjectRegistration;
import com.practice.universitysystem.service.grade.GradeService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@Slf4j
@ExtendWith(MockitoExtension.class)
class GradeServiceTest {

    @Mock
    GradeService gradeService;

    @Test
    void setStudentFinalGradeTest(@Mock StudentSubjectRegistration subjectRegistration) {
        when(gradeService.setStudentFinalGrade(anyLong(), anyLong(), anyDouble())).thenReturn(subjectRegistration);

        gradeService.setStudentFinalGrade(anyLong(), anyLong(), anyDouble());

        verify(gradeService).setStudentFinalGrade(anyLong(), anyLong(), anyDouble());
    }

    @Test
    void setStudentFinalGradeAutoTest(@Mock StudentSubjectRegistration subjectRegistration) {
        when(gradeService.setStudentFinalGradeAuto(anyLong(), anyLong())).thenReturn(subjectRegistration);

        gradeService.setStudentFinalGradeAuto(anyLong(), anyLong());

        verify(gradeService).setStudentFinalGradeAuto(anyLong(), anyLong());
    }

    @Test
    void getAllStudentAllGradesInSubjectTest() {
        List<Set<Grade>> list = new ArrayList<>();
        when(gradeService.getAllStudentAllGradesInSubject(anyLong())).thenReturn(list);

        gradeService.getAllStudentAllGradesInSubject(anyLong());

        verify(gradeService).getAllStudentAllGradesInSubject(anyLong());
    }

    @Test
    void getAllStudentAllGradesTest() {
        List<Set<Grade>> list = new ArrayList<>();
        when(gradeService.getAllStudentAllGrades()).thenReturn(list);

        gradeService.getAllStudentAllGrades();

        verify(gradeService).getAllStudentAllGrades();
    }

    @Test
    void getOneStudentGradesTest() {
        Set<Grade> grades = new HashSet<>();
        when(gradeService.getOneStudentGrades(anyLong(), anyLong())).thenReturn(grades);

        gradeService.getOneStudentGrades(anyLong(), anyLong());

        verify(gradeService).getOneStudentGrades(anyLong(), anyLong());
    }
}
