package com.practice.universitysystem.model;

import com.practice.universitysystem.repository.StudentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@SpringBootTest
public class StudentTests {

    @Autowired
    StudentRepository studentRepository;

    @Test
    @Transactional
    void createStudent() throws ParseException {
        Student student = new Student();
        student.setName("TEST_USER_NAME");
        student.setLastName("TEST_USER_LAST_NAME");
        student.setGovernmentId("1234567890");
        student.setEmail("test@testmail.com");
        student.setMobilePhone("1234567896");
        student.setLandPhone("1234567896");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        student.setBirthdate(dateFormat.parse("14-05-2000"));

        studentRepository.save(student);

        Assertions.assertTrue(studentRepository.count() > 0);

    }

    @Test
    @Transactional
    void deleteStudent() throws ParseException {
        Student student = new Student();
        student.setName("TEST_USER_NAME");
        student.setLastName("TEST_USER_LAST_NAME");
        student.setGovernmentId("1234567890");
        student.setEmail("test@testmail.com");
        student.setMobilePhone("1234567896");
        student.setLandPhone("1234567896");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        student.setBirthdate(dateFormat.parse("14-05-2000"));

        studentRepository.save(student);

        Assertions.assertEquals(1, studentRepository.count());

        studentRepository.delete(student);

        Assertions.assertEquals(0, studentRepository.count());

    }
}