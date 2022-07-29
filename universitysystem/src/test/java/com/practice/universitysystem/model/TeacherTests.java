package com.practice.universitysystem.model;

import com.practice.universitysystem.model.curriculum.subject.Subject;
import com.practice.universitysystem.model.users.teacher.teacher_asignation.TeacherAssignation;
import com.practice.universitysystem.model.users.teacher.Teacher;
import com.practice.universitysystem.repository.curriculum.subject.SubjectRepository;
import com.practice.universitysystem.repository.users.teacher.teacher_assignation.TeacherAssignationRepository;
import com.practice.universitysystem.repository.users.teacher.TeacherRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static com.practice.universitysystem.model.SubjectTests.getSubject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        teacher.setBirthdate(dateFormat.parse("14-05-2000"));
        return teacher;
    }

    @Autowired
    TeacherRepository teacherRepository;
    @Autowired
    TeacherAssignationRepository teacherAssignationRepository;
    @Autowired
    SubjectRepository subjectRepository;

    @Test
    @Transactional
    void createAndDeleteTeacherTest() throws ParseException {
        assertEquals(0, teacherRepository.count());
        Teacher teacher = getTeacher();

        teacherRepository.save(teacher);

        assertEquals(1, teacherRepository.count());
        teacherRepository.delete(teacher);
        assertEquals(0, teacherRepository.count());
    }

    @Test
    @Transactional
    void assignTeacherTest() throws ParseException {
        assertEquals(0, teacherRepository.count());
        assertEquals(0, subjectRepository.count());
        assertEquals(0, teacherAssignationRepository.count());

        Teacher teacher = getTeacher();
        teacherRepository.save(teacher);

        Subject subject = getSubject();
        subjectRepository.save(subject);

        TeacherAssignation assignation = new TeacherAssignation(teacher.getId(), subject.getId());

        assignation.setRoleInClass("MAIN_TEACHER");

        assertFalse(teacherAssignationRepository.existsById(assignation.getId()));

        teacherAssignationRepository.save(assignation);

        assertEquals(1, teacherAssignationRepository.count());

    }
}
