package com.practice.universitysystem.model;

import com.practice.universitysystem.model.student_subject.StudentSubjectRegistration;
import com.practice.universitysystem.repository.GradeRepository;
import com.practice.universitysystem.repository.StudentRepository;
import com.practice.universitysystem.repository.StudentSubjectRegistrationRepository;
import com.practice.universitysystem.repository.SubjectRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;

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
        return student;
    }

    @Test
    @Transactional
    void createAndDeleteStudentTest() throws ParseException {
        assertEquals(0, studentRepository.count());

        Student student = getStudent();

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
        studentRepository.save(student);

        Subject subject = getSubject();
        subjectRepository.save(subject);

        StudentSubjectRegistration subjectRegistration = new StudentSubjectRegistration(student.getId(), subject.getId());

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
        studentRepository.save(student);

        Subject subject = getSubject();
        subjectRepository.save(subject);

        StudentSubjectRegistration subjectRegistration = new StudentSubjectRegistration(student.getId(), subject.getId());

        assertFalse(studentSubjectRegistrationRepository.existsById(subjectRegistration.getId()));

        subjectRegistration.setSubjectGrades(new HashSet<>());

        studentSubjectRegistrationRepository.save(subjectRegistration);
        assertEquals(1, studentSubjectRegistrationRepository.count());


        Grade grade1 = new Grade();
        grade1.setDescription("GRADE_DESCRIPTION");
        grade1.setGradeValue(3.5);
        grade1.setPercentageOfFinalGrade(50);
        grade1.setRegistration(subjectRegistration);

        Grade grade2 = new Grade();
        grade2.setDescription("GRADE_DESCRIPTION");
        grade2.setGradeValue(5);
        grade2.setPercentageOfFinalGrade(50);
        grade2.setRegistration(subjectRegistration);

        subjectRegistration.getSubjectGrades().add(grade1);
        subjectRegistration.getSubjectGrades().add(grade2);

        studentSubjectRegistrationRepository.save(subjectRegistration);

        System.out.println(subjectRegistration);

        assertEquals(2, gradeRepository.count());

        assertEquals(2, studentSubjectRegistrationRepository.
                getReferenceById(subjectRegistration.getId()).getSubjectGrades().size());
    }
}
