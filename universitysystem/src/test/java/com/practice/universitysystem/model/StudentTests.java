package com.practice.universitysystem.model;

import com.practice.universitysystem.model.curriculum.subject.Grade;
import com.practice.universitysystem.model.curriculum.subject.Subject;
import com.practice.universitysystem.model.users.student.student_subject.StudentSubjectRegistration;
import com.practice.universitysystem.model.users.student.Student;
import com.practice.universitysystem.repository.curriculum.subject.GradeRepository;
import com.practice.universitysystem.repository.users.student.StudentRepository;
import com.practice.universitysystem.repository.users.student.student_subject.StudentSubjectRegistrationRepository;
import com.practice.universitysystem.repository.curriculum.subject.SubjectRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static com.practice.universitysystem.model.SubjectTests.getSubject;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class StudentTests {

    @Autowired
    StudentRepository studentRepository;
    @Autowired
    SubjectRepository subjectRepository;
    @Autowired
    StudentSubjectRegistrationRepository studentSubjectRegistrationRepository;
    @Autowired
    GradeRepository gradeRepository;

    private static Validator validator;

    @BeforeAll
    public static void validationSetup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    protected static Student getStudent() throws ParseException {
        Student student = new Student();
        student.setName("TEST_USER_NAME");
        student.setLastName("TEST_USER_LAST_NAME");
        student.setGovernmentId("1234567890");
        student.setEmail("test@testmail.com");
        student.setMobilePhone("1234567896");
        student.setLandPhone("1234567896");
        student.setBirthdate(LocalDate.parse("2000-05-14"));
        student.setUsername("TEST_USERNAME");
        student.setUserPassword("TEST_PASSWORD");
        student.setEnrollmentDate(LocalDate.now());
        return student;
    }

    @Test
    @Transactional
    void createAndDeleteStudentTest() throws ParseException {
        Student student = getStudent();
        Set<ConstraintViolation<Student>> constraintViolations = validator.validate(student);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }

        studentRepository.save(student);
        assertThat(studentRepository.findById(student.getId()).orElseThrow(), is(student));

        studentRepository.delete(student);
        assertThat(studentRepository.findById(student.getId()).isPresent(), is(false));
    }

    @Test
    @Transactional
    void registerSubjectTest() throws ParseException {

        Student student = getStudent();
        Set<ConstraintViolation<Student>> constraintViolations = validator.validate(student);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
        studentRepository.save(student);

        Subject subject = getSubject("SUBJECT1");
        Set<ConstraintViolation<Subject>> constraintViolationsSubject = validator.validate(subject);
        if (!constraintViolationsSubject.isEmpty()) {
            throw new ConstraintViolationException(constraintViolationsSubject);
        }
        subjectRepository.save(subject);

        StudentSubjectRegistration subjectRegistration = new StudentSubjectRegistration(student.getId(), subject.getId());
        subjectRegistration.setRegistrationDate(LocalDate.now());
        Set<ConstraintViolation<StudentSubjectRegistration>> constraintViolationsRegistration = validator.validate(subjectRegistration);
        if (!constraintViolationsRegistration.isEmpty()) {
            throw new ConstraintViolationException(constraintViolationsRegistration);
        }

        assertFalse(studentSubjectRegistrationRepository.existsById(subjectRegistration.getId()));

        studentSubjectRegistrationRepository.save(subjectRegistration);

        assertThat(studentSubjectRegistrationRepository.findAllBySubjectId(subject.getId()).get(0), is(subjectRegistration));
    }

    @Test
    @Transactional
    void registerSubjectGradeTest() throws ParseException {

        Student student = getStudent();
        Set<ConstraintViolation<Student>> constraintViolations = validator.validate(student);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
        studentRepository.save(student);

        Subject subject = getSubject("SUBJECT1");
        Set<ConstraintViolation<Subject>> constraintViolationsSubject = validator.validate(subject);
        if (!constraintViolationsSubject.isEmpty()) {
            throw new ConstraintViolationException(constraintViolationsSubject);
        }
        subjectRepository.save(subject);

        StudentSubjectRegistration subjectRegistration = new StudentSubjectRegistration(student.getId(), subject.getId());
        subjectRegistration.setRegistrationDate(LocalDate.now());
        Set<ConstraintViolation<StudentSubjectRegistration>> constraintViolationsRegistration = validator.validate(subjectRegistration);
        if (!constraintViolationsRegistration.isEmpty()) {
            throw new ConstraintViolationException(constraintViolationsRegistration);
        }
        assertFalse(studentSubjectRegistrationRepository.existsById(subjectRegistration.getId()));

        subjectRegistration.setSubjectGrades(new HashSet<>());

        studentSubjectRegistrationRepository.save(subjectRegistration);

        assertThat(studentSubjectRegistrationRepository.findAllBySubjectId(subject.getId()).get(0), is(subjectRegistration));

        Grade grade1 = new Grade();
        grade1.setDescription("GRADE_DESCRIPTION");
        grade1.setGradeValue(3.5);
        grade1.setPercentageOfFinalGrade(50D);
        grade1.setRegistrationId(subjectRegistration.getId());
        Set<ConstraintViolation<Grade>> constraintViolationsGrade1 = validator.validate(grade1);
        if (!constraintViolationsGrade1.isEmpty()) {
            throw new ConstraintViolationException(constraintViolationsGrade1);
        }

        gradeRepository.save(grade1);

        Grade grade2 = new Grade();
        grade2.setDescription("GRADE_DESCRIPTION");
        grade2.setGradeValue(5D);
        grade2.setPercentageOfFinalGrade(50D);
        grade2.setRegistrationId(subjectRegistration.getId());
        Set<ConstraintViolation<Grade>> constraintViolationsGrade2 = validator.validate(grade2);
        if (!constraintViolationsGrade2.isEmpty()) {
            throw new ConstraintViolationException(constraintViolationsGrade2);
        }

        gradeRepository.save(grade2);

        subjectRegistration.getSubjectGrades().add(grade1);
        subjectRegistration.getSubjectGrades().add(grade2);

        studentSubjectRegistrationRepository.save(subjectRegistration);

        System.out.println(subjectRegistration);

        assertThat(gradeRepository.findById(grade1.getId()).get(), is(grade1));
        assertThat(gradeRepository.findById(grade2.getId()).get(), is(grade2));

        assertThat(studentSubjectRegistrationRepository.
                getReferenceById(subjectRegistration.getId()).getSubjectGrades(), contains(grade1, grade2));
    }
}
