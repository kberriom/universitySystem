package com.practice.universitysystem.service;

import com.practice.universitysystem.dto.users.StudentDto;
import com.practice.universitysystem.model.users.student.Student;
import com.practice.universitysystem.repository.users.UniversityUserRepository;
import com.practice.universitysystem.repository.users.student.StudentRepository;
import com.practice.universitysystem.service.users.student.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    AuthService authService;
    @Mock
    UniversityUserRepository userRepository;
    @Mock
    StudentRepository instanceUserRepository;

    StudentService studentService;

    @BeforeEach
    void setup() {
        studentService = new StudentService(authService, userRepository, instanceUserRepository);
    }

    @Test
    void createUserTest() {
        StudentDto studentDto = new StudentDto();
        studentDto.setName("teststudent");
        studentDto.setLastName("teststudent lastname");
        studentDto.setGovernmentId("1111111111111");
        studentDto.setEmail("teststudent@university.com");
        studentDto.setMobilePhone("123456789");
        studentDto.setBirthdate(LocalDate.now());
        String rawPassword = "123456.";
        studentDto.setUserPassword(rawPassword);
        studentDto.setUsername("teststudent");
        when(authService.getEncodedPassword(studentDto.getUserPassword())).then(invocationOnMock -> new BCryptPasswordEncoder().encode(invocationOnMock.getArgument(0)));
        when(instanceUserRepository.save(any(Student.class))).then(AdditionalAnswers.returnsFirstArg());

        assertThat(studentService.createUser(studentDto).getUserPassword(), is(not(rawPassword)));
    }

    @Test
    void updateUserTest() {
        Student student = new Student();
        student.setName("teststudent");
        student.setLastName("teststudent lastname");
        student.setGovernmentId("1111111111111");
        student.setEmail("teststudent@university.com");
        student.setMobilePhone("123456789");
        student.setBirthdate(LocalDate.now());
        student.setUserPassword("123456.");
        student.setUsername("teststudent");
        student.setEnrollmentDate(LocalDate.now());

        StudentDto studentDto = new StudentDto();
        studentDto.setName("teststudent mod");
        studentDto.setLastName("teststudent lastname mod");
        studentDto.setGovernmentId("bad");
        studentDto.setEmail("bad");
        studentDto.setMobilePhone("123456789");
        studentDto.setLandPhone("123456789");
        studentDto.setBirthdate(LocalDate.parse("1980-10-10"));
        String rawPassword = "bad";
        studentDto.setUserPassword(rawPassword);
        studentDto.setUsername("bad");

        when(instanceUserRepository.save(any(Student.class))).then(AdditionalAnswers.returnsFirstArg());
        when(instanceUserRepository.findById(anyLong())).thenReturn(Optional.of(student));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(student));

        Student updateUser = studentService.updateUser(studentDto.getEmail(), studentDto);
        assertThat(updateUser.getEmail(), is(not(studentDto.getEmail())));
        assertThat(updateUser.getBirthdate(), is(not(studentDto.getBirthdate())));
        assertThat(updateUser.getUserPassword(), is(not(studentDto.getUserPassword())));
        assertThat(updateUser.getUsername(), is(not(studentDto.getUsername())));
        assertThat(updateUser.getName(), is(studentDto.getName()));
        assertThat(updateUser.getLastName(), is(studentDto.getLastName()));
        assertThat(updateUser.getMobilePhone(), is(studentDto.getMobilePhone()));
        assertThat(updateUser.getLandPhone(), is(studentDto.getLandPhone()));
    }

    @Test
    void updateUserAdminTest() {
        Student student = new Student();
        student.setId(1);
        student.setName("teststudent");
        student.setLastName("teststudent lastname");
        student.setGovernmentId("1111111111111");
        student.setEmail("teststudent@university.com");
        student.setMobilePhone("123456789");
        student.setBirthdate(LocalDate.now());
        student.setUserPassword("123456.");
        student.setUsername("teststudent");
        student.setEnrollmentDate(LocalDate.now());

        StudentDto studentDto = new StudentDto();
        studentDto.setName("teststudent mod");
        studentDto.setLastName("teststudent lastname mod");
        studentDto.setGovernmentId("update");
        studentDto.setEmail("update@update.com");
        studentDto.setMobilePhone("123456789");
        studentDto.setLandPhone("123456789");
        studentDto.setBirthdate(LocalDate.parse("1980-10-10"));
        String rawPassword = "update";
        studentDto.setUserPassword(rawPassword);
        studentDto.setUsername("update");

        when(instanceUserRepository.save(any(Student.class))).then(AdditionalAnswers.returnsFirstArg());
        when(instanceUserRepository.findById(anyLong())).thenReturn(Optional.of(student));

        Student updateUser = studentService.updateUser(1, studentDto);
        assertThat(updateUser.getEmail(), is(studentDto.getEmail()));
        assertThat(updateUser.getBirthdate(), is(studentDto.getBirthdate()));
        assertThat(updateUser.getUserPassword(), is(not(studentDto.getUserPassword())));
        assertThat(updateUser.getUsername(), is(studentDto.getUsername()));
        assertThat(updateUser.getName(), is(studentDto.getName()));
        assertThat(updateUser.getLastName(), is(studentDto.getLastName()));
        assertThat(updateUser.getMobilePhone(), is(studentDto.getMobilePhone()));
        assertThat(updateUser.getLandPhone(), is(studentDto.getLandPhone()));
    }

    @Test
    void getUserTest() {
        Student student = new Student();
        student.setId(1);
        student.setName("teststudent");
        student.setLastName("teststudent lastname");
        student.setGovernmentId("1111111111111");
        student.setEmail("teststudent@university.com");
        student.setMobilePhone("123456789");
        student.setBirthdate(LocalDate.now());
        student.setUserPassword("123456.");
        student.setUsername("teststudent");
        student.setEnrollmentDate(LocalDate.now());

        when(instanceUserRepository.findById(anyLong())).thenReturn(Optional.of(student));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(student));

        assertThat(studentService.getUser(student.getEmail()), is(student));
    }

    @Test
    void getAllUsersTest() {
        Student student = mock(Student.class);
        when(instanceUserRepository.findAll()).thenReturn(List.of(mock(Student.class), student, mock(Student.class)));

        assertThat(studentService.getAllUsers(), hasItem(student));
    }

    @Test
    void getUserPaginatedListTest() {
        Student student = mock(Student.class);
        Student student2 = mock(Student.class);
        ArrayList<Student> list = new ArrayList<>();
        list.add(student);
        list.add(student2);
        when(instanceUserRepository.findAll(isA(Pageable.class))).thenReturn(new PageImpl<>(list.stream().skip(list.size()-1).toList()));

        assertThat(studentService.getUserPaginatedList(1,1).stream().skip(1).collect(Collectors.toList()),
                anyOf(hasItem(student), hasItem(student2)));
    }
}
