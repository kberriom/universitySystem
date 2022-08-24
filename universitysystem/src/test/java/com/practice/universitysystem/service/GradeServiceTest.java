package com.practice.universitysystem.service;

import com.practice.universitysystem.dto.GradeDto;
import com.practice.universitysystem.model.curriculum.subject.Grade;
import com.practice.universitysystem.model.users.student.student_subject.StudentSubjectRegistration;
import com.practice.universitysystem.model.users.student.student_subject.StudentSubjectRegistrationId;
import com.practice.universitysystem.repository.curriculum.subject.GradeRepository;
import com.practice.universitysystem.repository.curriculum.subject.SubjectRepository;
import com.practice.universitysystem.repository.users.student.student_subject.StudentSubjectRegistrationRepository;
import com.practice.universitysystem.service.grade.GradeService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.Mock;
import org.mockito.hamcrest.MockitoHamcrest;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class GradeServiceTest {

    GradeService gradeService;

    @Mock
    GradeRepository gradeRepository;
    @Mock
    SubjectRepository subjectRepository;
    @Mock
    StudentSubjectRegistrationRepository registrationRepository;

    @BeforeEach
    void setup() {
        gradeService = new GradeService(gradeRepository, registrationRepository, subjectRepository);
    }

    @Test
    void setStudentFinalGradeTest() {
        StudentSubjectRegistration subjectRegistration = new StudentSubjectRegistration();
        subjectRegistration.setRegistrationDate(LocalDate.now());

        when(registrationRepository.findById(any(StudentSubjectRegistrationId.class))).thenReturn(Optional.of(subjectRegistration));
        when(registrationRepository.save(any(StudentSubjectRegistration.class))).then(AdditionalAnswers.returnsFirstArg());

        assertThat(gradeService.setStudentFinalGrade(1L, 1L, 3.2D), instanceOf(StudentSubjectRegistration.class));
    }

    @Test
    void setStudentFinalGradeAutoTest(@Mock StudentSubjectRegistrationId registrationId) {
        StudentSubjectRegistration subjectRegistration = new StudentSubjectRegistration();
        subjectRegistration.setId(registrationId);
        subjectRegistration.setRegistrationDate(LocalDate.now());
        subjectRegistration.setSubjectGrades(new HashSet<>());

        Grade grade1 = new Grade();
        grade1.setId(1L);
        grade1.setPercentageOfFinalGrade(25.0D);
        grade1.setGradeValue(2.5D);
        grade1.setDescription("Description");
        grade1.setRegistrationId(subjectRegistration.getId());

        Grade grade2 = new Grade();
        grade2.setId(2L);
        grade2.setPercentageOfFinalGrade(25.0D);
        grade2.setGradeValue(2.5D);
        grade2.setDescription("Description");
        grade2.setRegistrationId(subjectRegistration.getId());

        Grade grade3 = new Grade();
        grade3.setId(3L);
        grade3.setPercentageOfFinalGrade(25.0D);
        grade3.setGradeValue(2.5D);
        grade3.setDescription("Description");
        grade3.setRegistrationId(subjectRegistration.getId());

        Grade grade4 = new Grade();
        grade4.setId(4L);
        grade4.setPercentageOfFinalGrade(25.0D);
        grade4.setGradeValue(4.5D);
        grade4.setDescription("Description");
        grade4.setRegistrationId(subjectRegistration.getId());

        subjectRegistration.getSubjectGrades().add(grade1);
        subjectRegistration.getSubjectGrades().add(grade2);
        subjectRegistration.getSubjectGrades().add(grade3);
        subjectRegistration.getSubjectGrades().add(grade4);

        when(registrationRepository.findById(any(StudentSubjectRegistrationId.class))).thenReturn(Optional.of(subjectRegistration));
        when(registrationRepository.save(any(StudentSubjectRegistration.class))).then(AdditionalAnswers.returnsFirstArg());

        assertThat(gradeService.setStudentFinalGradeAuto(1L, 1L).getFinalGrade(), is(3D));
    }

    @Test
    void addStudentGradeTest() {
        StudentSubjectRegistration subjectRegistration = new StudentSubjectRegistration();
        subjectRegistration.setId(mock(StudentSubjectRegistrationId.class));
        subjectRegistration.setRegistrationDate(LocalDate.now());
        subjectRegistration.setSubjectGrades(new HashSet<>());

        GradeDto gradeDto = new GradeDto();
        gradeDto.setGradeValue(5D);
        gradeDto.setDescription("Description");
        gradeDto.setPercentageOfFinalGrade(100D);

        when(registrationRepository.findById(any(StudentSubjectRegistrationId.class))).thenReturn(Optional.of(subjectRegistration));
        when(registrationRepository.save(any(StudentSubjectRegistration.class))).then(AdditionalAnswers.returnsFirstArg());

        assertThat(gradeService.addStudentGrade(1L, 1L, gradeDto).getSubjectGrades().size(), is(1));
    }

    @Test
    void modifyStudentGradeTest(@Mock StudentSubjectRegistrationId registrationId) {
        StudentSubjectRegistration subjectRegistration = new StudentSubjectRegistration();
        subjectRegistration.setId(registrationId);
        subjectRegistration.setRegistrationDate(LocalDate.now());
        subjectRegistration.setSubjectGrades(new HashSet<>());

        Grade grade = new Grade();
        grade.setId(1L);
        grade.setGradeValue(5D);
        grade.setDescription("Description");
        grade.setPercentageOfFinalGrade(59D);
        grade.setRegistrationId(registrationId);

        Grade gradeCopy = new Grade();
        gradeCopy.setId(1L);
        gradeCopy.setGradeValue(5D);
        gradeCopy.setDescription("Description");
        gradeCopy.setPercentageOfFinalGrade(59D);
        gradeCopy.setRegistrationId(registrationId);

        subjectRegistration.getSubjectGrades().add(grade);

        when(registrationRepository.findById(any(StudentSubjectRegistrationId.class))).thenReturn(Optional.of(subjectRegistration));
        when(registrationRepository.save(any(StudentSubjectRegistration.class))).then(AdditionalAnswers.returnsFirstArg());
        when(gradeRepository.findById(anyLong())).thenReturn(Optional.of(grade));
        when(gradeRepository.save(any(Grade.class))).then(invocationOnMock -> {
            grade.setId(grade.getId()+1);
            return grade;
        });

        GradeDto gradeMod = new GradeDto();
        gradeMod.setGradeValue(1D);
        gradeMod.setDescription("Description mod");
        gradeMod.setPercentageOfFinalGrade(99D);

        assertThat(gradeService.modifyStudentGrade(1L, 1L, 1L, gradeMod).getSubjectGrades(), containsInAnyOrder(not(gradeCopy)));
    }

    @Test
    void getAllStudentAllGradesInSubjectTest(@Mock StudentSubjectRegistrationId registrationId) {
        StudentSubjectRegistration subjectRegistration = new StudentSubjectRegistration();
        subjectRegistration.setId(registrationId);
        subjectRegistration.setRegistrationDate(LocalDate.now());
        subjectRegistration.setSubjectGrades(new HashSet<>());

        Grade grade = new Grade();
        grade.setId(1L);
        grade.setGradeValue(5D);
        grade.setDescription("Description");
        grade.setPercentageOfFinalGrade(59D);
        grade.setRegistrationId(registrationId);
        subjectRegistration.getSubjectGrades().add(grade);

        when(subjectRepository.existsById(MockitoHamcrest.longThat(is(1L)))).thenReturn(true);
        when(registrationRepository.findAllBySubjectId(MockitoHamcrest.longThat(is(1L)))).thenReturn((List.of(subjectRegistration)));

        assertThat(gradeService.getAllStudentAllGradesInSubject(1L), containsInAnyOrder(Set.of(grade)));
    }

    @Test
    void getAllStudentAllGradesTest() {
        StudentSubjectRegistrationId registrationId = mock(StudentSubjectRegistrationId.class);
        StudentSubjectRegistration subjectRegistration = new StudentSubjectRegistration();
        subjectRegistration.setId(registrationId);
        subjectRegistration.setRegistrationDate(LocalDate.now());
        subjectRegistration.setSubjectGrades(new HashSet<>());

        Grade grade = new Grade();
        grade.setId(1L);
        grade.setGradeValue(5D);
        grade.setDescription("Description");
        grade.setPercentageOfFinalGrade(59D);
        grade.setRegistrationId(registrationId);
        subjectRegistration.getSubjectGrades().add(grade);

        StudentSubjectRegistrationId registrationId2 = mock(StudentSubjectRegistrationId.class);
        StudentSubjectRegistration subjectRegistration2 = new StudentSubjectRegistration();
        subjectRegistration2.setId(registrationId2);
        subjectRegistration2.setRegistrationDate(LocalDate.now());
        subjectRegistration2.setSubjectGrades(new HashSet<>());

        Grade grade2 = new Grade();
        grade2.setId(2L);
        grade2.setGradeValue(3.5D);
        grade2.setDescription("Description 2");
        grade2.setPercentageOfFinalGrade(59D);
        grade2.setRegistrationId(registrationId2);
        subjectRegistration2.getSubjectGrades().add(grade2);

        when(registrationRepository.findAll()).thenReturn(List.of(subjectRegistration, subjectRegistration2));

        assertThat(gradeService.getAllStudentAllGrades(), is(List.of(Set.of(grade), Set.of(grade2))));
    }

    @Test
    void getOneStudentGradesTest() {
        StudentSubjectRegistrationId registrationId = mock(StudentSubjectRegistrationId.class);
        StudentSubjectRegistration subjectRegistration = new StudentSubjectRegistration();
        subjectRegistration.setId(registrationId);
        subjectRegistration.setRegistrationDate(LocalDate.now());
        subjectRegistration.setSubjectGrades(new HashSet<>());

        Grade grade = new Grade();
        grade.setId(1L);
        grade.setGradeValue(5D);
        grade.setDescription("Description");
        grade.setPercentageOfFinalGrade(59D);
        grade.setRegistrationId(registrationId);
        subjectRegistration.getSubjectGrades().add(grade);

        when(registrationRepository.findById(any(StudentSubjectRegistrationId.class))).thenReturn(Optional.of(subjectRegistration));

        assertThat(gradeService.getOneStudentGrades(registrationId.getSubjectId(), registrationId.getStudentUserId()), containsInAnyOrder(grade));
    }
}
