package com.practice.universitysystem.service;

import com.practice.universitysystem.dto.SubjectDto;
import com.practice.universitysystem.model.curriculum.subject.Subject;
import com.practice.universitysystem.model.users.student.Student;
import com.practice.universitysystem.model.users.student.student_subject.StudentSubjectRegistration;
import com.practice.universitysystem.model.users.student.student_subject.StudentSubjectRegistrationId;
import com.practice.universitysystem.model.users.teacher.Teacher;
import com.practice.universitysystem.model.users.teacher.teacher_asignation.TeacherAssignation;
import com.practice.universitysystem.repository.curriculum.subject.SubjectRepository;
import com.practice.universitysystem.repository.users.student.student_subject.StudentSubjectRegistrationRepository;
import com.practice.universitysystem.repository.users.teacher.teacher_assignation.TeacherAssignationRepository;
import com.practice.universitysystem.service.subject.SubjectService;
import com.practice.universitysystem.service.users.student.StudentService;
import com.practice.universitysystem.service.users.teacher.TeacherService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.in;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class SubjectServiceTest {

    SubjectService subjectService;

    @Mock
    SubjectRepository subjectRepository;
    @Mock
    StudentSubjectRegistrationRepository registrationRepository;
    @Mock
    TeacherAssignationRepository teacherAssignationRepository;
    @Mock
    StudentService studentService;
    @Mock
    TeacherService teacherService;

    @BeforeEach
    void setup() {
        subjectService = new SubjectService(subjectRepository, registrationRepository, teacherAssignationRepository,
                studentService, teacherService);
    }

    @Test
    void createSubject() {
        SubjectDto subjectDto = new SubjectDto();
        subjectDto.setName("test subject");
        subjectDto.setDescription("test description");
        subjectDto.setStartDate(LocalDate.parse("2026-05-15"));
        subjectDto.setEndDate(LocalDate.parse ("2027-06-16"));
        subjectDto.setRemote(true);
        subjectDto.setOnSite(true);
        subjectDto.setRoomLocation("18-30");
        subjectDto.setCreditsValue(3);

        when(subjectRepository.save(any(Subject.class))).then(AdditionalAnswers.returnsFirstArg());

        Subject subject = subjectService.createSubject(subjectDto);
        assertThat(subject.getName(), is(subjectDto.getName()));
        assertThat(subject.getDescription(), is(subjectDto.getDescription()));
        assertThat(subject.getStartDate(), is(subjectDto.getStartDate()));
        assertThat(subject.getEndDate(), is(subjectDto.getEndDate()));
        assertThat(subject.getRemote(), is(subjectDto.getRemote()));
        assertThat(subject.getOnSite(), is(subjectDto.getOnSite()));
        assertThat(subject.getRoomLocation(), is(subjectDto.getRoomLocation()));
        assertThat(subject.getCreditsValue(), is(subjectDto.getCreditsValue()));
    }

    @Test
    void getSubjectTest() {
        Subject subject = mock(Subject.class);

        when(subjectRepository.findByName(subject.getName())).thenReturn(Optional.of(subject));

        assertThat(subjectService.getSubject(subject.getName()), is(subject));
    }

    @Test
    void getAllSubjectTest() {
        List<Subject> subjects = new ArrayList<>();
        subjects.add(mock(Subject.class));
        subjects.add(mock(Subject.class));
        subjects.add(mock(Subject.class));

        when(subjectRepository.findAll()).thenReturn(subjects);

        assertThat(subjectService.getAllSubjects(), everyItem(is(in(subjects))));
    }

    @Test
    void getPaginatedSubjectTest() {
        Subject subject = mock(Subject.class);
        Subject subject2 = mock(Subject.class);
        ArrayList<Subject> list = new ArrayList<>();
        list.add(subject);
        list.add(subject2);
        when(subjectRepository.findAll(isA(Pageable.class))).thenReturn(new PageImpl<>(list.stream().skip(list.size()-1).toList()));

        assertThat(subjectService.getPaginatedSubjects(1,1).stream().skip(1).collect(Collectors.toList()),
                anyOf(hasItem(subject), hasItem(subject2)));
    }

    @Test
    void updateSubjectTest() {
        Subject subject = new Subject();
        subject.setName("test subject");
        subject.setDescription("test description");
        subject.setStartDate(LocalDate.parse("2026-05-15"));
        subject.setEndDate(LocalDate.parse ("2027-06-16"));
        subject.setRemote(true);
        subject.setOnSite(true);
        subject.setRoomLocation("18-30");
        subject.setCreditsValue(3);

        SubjectDto subjectDto = new SubjectDto();
        subjectDto.setName("test subject mod");
        subjectDto.setDescription("test description mod");
        subjectDto.setStartDate(LocalDate.parse("2020-05-15"));
        subjectDto.setEndDate(LocalDate.parse ("2023-06-16"));
        subjectDto.setRemote(true);
        subjectDto.setOnSite(false);
        subjectDto.setRoomLocation("18-301");
        subjectDto.setCreditsValue(3);

        when(subjectRepository.findByName(subject.getName())).thenReturn(Optional.of(subject));
        when(subjectRepository.save(any(Subject.class))).then(AdditionalAnswers.returnsFirstArg());

        Subject updateSubject = subjectService.updateSubject(subject.getName(), subjectDto);
        assertThat(updateSubject.getId(), is(subject.getId()));
        assertThat(updateSubject.getName(), is(subjectDto.getName()));
        assertThat(updateSubject.getDescription(), is(subjectDto.getDescription()));
        assertThat(updateSubject.getStartDate(), is(subjectDto.getStartDate()));
        assertThat(updateSubject.getEndDate(), is(subjectDto.getEndDate()));
        assertThat(updateSubject.getRemote(), is(subjectDto.getRemote()));
        assertThat(updateSubject.getOnSite(), is(subjectDto.getOnSite()));
        assertThat(updateSubject.getRoomLocation(), is(subjectDto.getRoomLocation()));
        assertThat(updateSubject.getCreditsValue(), is(subjectDto.getCreditsValue()));
    }

    @Test
    void getAllRegisteredStudentsTest() {
        Subject subject = mock(Subject.class);
        StudentSubjectRegistration student = mock(StudentSubjectRegistration.class);
        StudentSubjectRegistration student2 = mock(StudentSubjectRegistration.class);

        when(subjectRepository.findByName(subject.getName())).thenReturn(Optional.of(subject));
        when(registrationRepository.findAllBySubjectId(subject.getId())).thenReturn(List.of(student, student2));

        assertThat(subjectService.getAllRegisteredStudents(subject.getName()), is(List.of(student, student2)));
    }

    @Test
    void addStudentToSubjectTest() {
        Student student = mock(Student.class);
        Subject subject = mock(Subject.class);

        when(studentService.getUser(student.getId())).thenReturn(student);
        when(subjectRepository.findByName(subject.getName())).thenReturn(Optional.of(subject));
        when(registrationRepository.findById(any(StudentSubjectRegistrationId.class))).thenReturn(Optional.empty());
        when(registrationRepository.save(any(StudentSubjectRegistration.class))).then(AdditionalAnswers.returnsFirstArg());

        StudentSubjectRegistration subjectRegistration = subjectService.addStudentToSubject(student.getId(), subject.getName());
        assertThat(subjectRegistration.getId().getSubjectId(), is(subject.getId()));
        assertThat(subjectRegistration.getId().getStudentUserId(), is(student.getId()));
    }

    @Test
    void addTeacherToSubjectTest() {
        Teacher teacher = mock(Teacher.class);
        Subject subject = mock(Subject.class);

        when(teacherService.getUser(teacher.getId())).thenReturn(teacher);
        when(subjectRepository.findByName(subject.getName())).thenReturn(Optional.of(subject));
        when(teacherAssignationRepository.save(any(TeacherAssignation.class))).then(AdditionalAnswers.returnsFirstArg());

        String role = "teacher";
        assertThat(subjectService.addTeacherToSubject(teacher.getId(), subject.getName(), role).getRoleInClass(), is(role));
    }

    @Test
    void getAllTeachersTest() {
        Subject subject = mock(Subject.class);
        TeacherAssignation teacherAssignation = mock(TeacherAssignation.class);

        when(subjectRepository.findByName(subject.getName())).thenReturn(Optional.of(subject));
        when(teacherAssignationRepository.findAllBySubjectId(subject.getId())).thenReturn(List.of(teacherAssignation));

        assertThat(subjectService.getAllTeachers(subject.getName()), containsInAnyOrder(teacherAssignation));
    }

    @Test
    void modifyTeacherRoleInSubjectTest() {
        Subject subject = mock(Subject.class);
        Teacher teacher = mock(Teacher.class);
        TeacherAssignation teacherAssignation = new TeacherAssignation(teacher.getId(), subject.getId());
        String oldRole = "oldRole";
        teacherAssignation.setRoleInClass(oldRole);

        when(subjectRepository.findByName(subject.getName())).thenReturn(Optional.of(subject));
        when(teacherAssignationRepository.findById(teacherAssignation.getId())).thenReturn(Optional.of(teacherAssignation));
        when(teacherAssignationRepository.save(any(TeacherAssignation.class))).then(AdditionalAnswers.returnsFirstArg());

        assertThat(subjectService.modifyTeacherRoleInSubject(teacher.getId(), subject.getName(), "newRole")
                .getRoleInClass(), is(not(oldRole)));
    }
}
