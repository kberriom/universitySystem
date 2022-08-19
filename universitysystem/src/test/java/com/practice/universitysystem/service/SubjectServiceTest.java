package com.practice.universitysystem.service;

import com.practice.universitysystem.dto.SubjectDto;
import com.practice.universitysystem.model.curriculum.subject.Subject;
import com.practice.universitysystem.model.users.student.student_subject.StudentSubjectRegistration;
import com.practice.universitysystem.model.users.teacher.teacher_asignation.TeacherAssignation;
import com.practice.universitysystem.service.subject.SubjectService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@Slf4j
@ExtendWith(MockitoExtension.class)
class SubjectServiceTest {

    @Mock
    SubjectService subjectService;

    @Test
    void createSubject(@Mock SubjectDto subjectDto) {
        when(subjectService.createSubject(any(SubjectDto.class))).thenReturn(any(Subject.class));

        subjectService.createSubject(subjectDto);

        verify(subjectService).createSubject(any(SubjectDto.class));
    }

    @Test
    void getSubjectTest() {
        when(subjectService.getSubject(anyString())).thenReturn(any(Subject.class));

        subjectService.getSubject("name");

        verify(subjectService).getSubject(anyString());
    }

    @Test
    void getAllSubjectTest() {
        List<Subject> list = new ArrayList<>();
        when(subjectService.getAllSubjects()).thenReturn(list);

        subjectService.getAllSubjects();

        verify(subjectService).getAllSubjects();
    }

    @Test
    void getPaginatedSubjectTest() {
        List<Object> list = new ArrayList<>();
        when(subjectService.getPaginatedSubjects(anyInt(), anyInt())).thenReturn(list);

        subjectService.getPaginatedSubjects(1, 2);

        verify(subjectService).getPaginatedSubjects(anyInt(), anyInt());
    }

    @Test
    void updateSubjectTest(@Mock SubjectDto subjectDto) {
        when(subjectService.updateSubject(anyString(), any(SubjectDto.class))).thenReturn(any(Subject.class));

        subjectService.updateSubject(anyString(), subjectDto);

        verify(subjectService).updateSubject(anyString(), any(SubjectDto.class));
    }

    @Test
    void addStudentToSubjectTest(@Mock StudentSubjectRegistration subjectRegistration) {
        when(subjectService.addStudentToSubject(anyLong(), anyString())).thenReturn(subjectRegistration);

        subjectService.addStudentToSubject(anyLong(), anyString());

        verify(subjectService).addStudentToSubject(anyLong(), anyString());
    }

    @Test
    void addTeacherToSubjectTest(@Mock TeacherAssignation teacherAssignation) {
        when(subjectService.addTeacherToSubject(anyLong(), anyString(), anyString())).thenReturn(teacherAssignation);

        subjectService.addTeacherToSubject(anyLong(), anyString(), anyString());

        verify(subjectService).addTeacherToSubject(anyLong(), anyString(), anyString());
    }

    @Test
    void getAllTeachersTest() {
        List<TeacherAssignation> list = new ArrayList<>();
        when(subjectService.getAllTeachers(anyString())).thenReturn(list);

        subjectService.getAllTeachers(anyString());

        verify(subjectService).getAllTeachers(anyString());
    }

    @Test
    void modifyTeacherRoleInSubjectTest(@Mock TeacherAssignation teacherAssignation) {
        when(subjectService.modifyTeacherRoleInSubject(anyLong(), anyString(), anyString())).thenReturn(teacherAssignation);

        subjectService.modifyTeacherRoleInSubject(anyLong(), anyString(), anyString());

        verify(subjectService).modifyTeacherRoleInSubject(anyLong(), anyString(), anyString());
    }
}
