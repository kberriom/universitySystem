package com.practice.universitysystem.model;

import com.practice.universitysystem.model.curriculum.subject.Subject;
import com.practice.universitysystem.model.users.teacher.teacher_asignation.TeacherAssignation;
import com.practice.universitysystem.model.users.teacher.Teacher;
import com.practice.universitysystem.model.users.teacher.teacher_asignation.TeacherAssignationId;
import com.practice.universitysystem.repository.curriculum.subject.SubjectRepository;
import com.practice.universitysystem.repository.users.teacher.teacher_assignation.TeacherAssignationRepository;
import com.practice.universitysystem.repository.users.teacher.TeacherRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Set;

import static com.practice.universitysystem.model.SubjectTests.getSubject;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest
class TeacherTests {

    protected Teacher getTeacher() throws ParseException {
        Teacher teacher = new Teacher();
        teacher.setName("TEST_USER_NAME");
        teacher.setLastName("TEST_USER_LAST_NAME");
        teacher.setGovernmentId("1234567890");
        teacher.setEmail("test@testmail.com");
        teacher.setMobilePhone("1234567896");
        teacher.setLandPhone("1234567896");
        teacher.setDepartment("TEST_DEPARTMENT");
        teacher.setBirthdate(LocalDate.parse("2000-05-14"));
        teacher.setUsername("TEACHER_USERNAME");
        teacher.setUserPassword("PASSWORD_EXAMPLE");
        teacher.setEnrollmentDate(LocalDate.now());
        return teacher;
    }

    @Autowired
    TeacherRepository teacherRepository;
    @Autowired
    TeacherAssignationRepository teacherAssignationRepository;
    @Autowired
    SubjectRepository subjectRepository;

    private static Validator validator;

    @BeforeAll
    public static void validationSetup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @Transactional
    void createAndDeleteTeacherTest() throws ParseException {

        Teacher teacher = getTeacher();
        Set<ConstraintViolation<Teacher>> constraintViolations = validator.validate(teacher);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }

        teacherRepository.save(teacher);
        assertThat(teacherRepository.findById(teacher.getId()).orElseThrow(), is(teacher));

        teacherRepository.delete(teacher);
        assertThat(teacherRepository.findById(teacher.getId()).isPresent(), is(false));
    }

    @Test
    @Transactional
    void assignTeacherTest() throws ParseException {

        Teacher teacher = getTeacher();
        Set<ConstraintViolation<Teacher>> constraintViolationsTeacher = validator.validate(teacher);
        if (!constraintViolationsTeacher.isEmpty()) {
            throw new ConstraintViolationException(constraintViolationsTeacher);
        }
        teacherRepository.save(teacher);

        Subject subject = getSubject("SUBJECT1");
        Set<ConstraintViolation<Subject>> constraintViolationsSubject = validator.validate(subject);
        if (!constraintViolationsSubject.isEmpty()) {
            throw new ConstraintViolationException(constraintViolationsSubject);
        }
        subjectRepository.save(subject);

        TeacherAssignation assignation = new TeacherAssignation(teacher.getId(), subject.getId());
        assignation.setRoleInClass("MAIN_TEACHER");
        Set<ConstraintViolation<TeacherAssignation>> constraintViolationsAssignation = validator.validate(assignation);
        if (!constraintViolationsAssignation.isEmpty()) {
            throw new ConstraintViolationException(constraintViolationsAssignation);
        }

        assertThat(teacherAssignationRepository.existsById(assignation.getId()), is(false));

        teacherAssignationRepository.save(assignation);

        TeacherAssignationId teacherAssignationId = new TeacherAssignationId();
        teacherAssignationId.setTeacherUserId(teacher.getId());
        teacherAssignationId.setSubjectId(subject.getId());
        assertThat(teacherAssignationRepository.findById(teacherAssignationId).orElseThrow(), is(assignation));
    }
}
