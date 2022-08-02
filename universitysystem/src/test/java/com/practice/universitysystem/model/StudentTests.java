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
import javax.validation.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static com.practice.universitysystem.model.SubjectTests.getSubject;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        student.setBirthdate(dateFormat.parse("14-05-2000"));
        student.setUsername("TEST_USERNAME");
        student.setUserPassword("TEST_PASSWORD");
        student.setEnrollmentDate(new Date());
        return student;
    }

    @Test
    @Transactional
    void createAndDeleteStudentTest() throws ParseException {
        assertEquals(0, studentRepository.count());

        Student student = getStudent();
        Set<ConstraintViolation<Student>> constraintViolations = validator.validate(student);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }

        studentRepository.save(student);

        assertEquals(1, studentRepository.count());

        studentRepository.delete(student);

        assertEquals(0, studentRepository.count());

    }

    @Test
    @Transactional
    void registerSubjectTest() throws ParseException {
        assertEquals(0, studentRepository.count());
        assertEquals(0, subjectRepository.count());
        assertEquals(0, studentSubjectRegistrationRepository.count());

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
        subjectRegistration.setRegistrationDate(new Date());
        Set<ConstraintViolation<StudentSubjectRegistration>> constraintViolationsRegistration = validator.validate(subjectRegistration);
        if (!constraintViolationsRegistration.isEmpty()) {
            throw new ConstraintViolationException(constraintViolationsRegistration);
        }

        assertFalse(studentSubjectRegistrationRepository.existsById(subjectRegistration.getId()));

        studentSubjectRegistrationRepository.save(subjectRegistration);

        assertEquals(1, studentSubjectRegistrationRepository.count());

    }

    @Test
    @Transactional
    void registerSubjectGradeTest() throws ParseException {
        assertEquals(0, studentRepository.count());
        assertEquals(0, subjectRepository.count());
        assertEquals(0, studentSubjectRegistrationRepository.count());
        assertEquals(0, gradeRepository.count());

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
        subjectRegistration.setRegistrationDate(new Date());
        Set<ConstraintViolation<StudentSubjectRegistration>> constraintViolationsRegistration = validator.validate(subjectRegistration);
        if (!constraintViolationsRegistration.isEmpty()) {
            throw new ConstraintViolationException(constraintViolationsRegistration);
        }
        assertFalse(studentSubjectRegistrationRepository.existsById(subjectRegistration.getId()));

        subjectRegistration.setSubjectGrades(new HashSet<>());

        studentSubjectRegistrationRepository.save(subjectRegistration);
        assertEquals(1, studentSubjectRegistrationRepository.count());


        Grade grade1 = new Grade();
        grade1.setDescription("GRADE_DESCRIPTION");
        grade1.setGradeValue(3.5);
        grade1.setPercentageOfFinalGrade(50);
        grade1.setRegistrationId(subjectRegistration.getId());
        Set<ConstraintViolation<Grade>> constraintViolationsGrade1 = validator.validate(grade1);
        if (!constraintViolationsGrade1.isEmpty()) {
            throw new ConstraintViolationException(constraintViolationsGrade1);
        }

        Grade grade2 = new Grade();
        grade2.setDescription("GRADE_DESCRIPTION");
        grade2.setGradeValue(5);
        grade2.setPercentageOfFinalGrade(50);
        grade2.setRegistrationId(subjectRegistration.getId());
        Set<ConstraintViolation<Grade>> constraintViolationsGrade2 = validator.validate(grade2);
        if (!constraintViolationsGrade2.isEmpty()) {
            throw new ConstraintViolationException(constraintViolationsGrade2);
        }

        subjectRegistration.getSubjectGrades().add(grade1);
        subjectRegistration.getSubjectGrades().add(grade2);

        studentSubjectRegistrationRepository.save(subjectRegistration);

        System.out.println(subjectRegistration);

        assertEquals(2, gradeRepository.count());

        assertEquals(2, studentSubjectRegistrationRepository.
                getReferenceById(subjectRegistration.getId()).getSubjectGrades().size());
    }
}
