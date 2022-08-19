package com.practice.universitysystem.service;

import com.practice.universitysystem.dto.users.StudentDto;
import com.practice.universitysystem.model.users.student.Student;
import com.practice.universitysystem.service.users.student.StudentService;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@Slf4j
@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    StudentService studentService;

    @Test
    void createUserTest(@Mock StudentDto studentDto) {
        when(studentService.createUser(any(StudentDto.class))).thenReturn(any(Student.class));

        studentService.createUser(studentDto);

        verify(studentService).createUser(studentDto);
    }

    @Test
    void updateUserTest(@Mock StudentDto studentDto) {
        when(studentService.updateUser(anyString(), any(StudentDto.class))).thenReturn(any(Student.class));

        studentService.updateUser(anyString(), studentDto);

        verify(studentService).updateUser(anyString(), any(StudentDto.class));
    }

    @Test
    void getUserTest() {
        when(studentService.getUser(anyString())).thenReturn(any(Student.class));

        studentService.getUser("student@university.com");

        verify(studentService).getUser(anyString());
    }

    @Test
    void getAllUsersTest() {
        List<Student> list = new ArrayList<>();
        when(studentService.getAllUsers()).thenReturn(list);

        studentService.getAllUsers();

        verify(studentService).getAllUsers();
    }

    @Test
    void getUserPaginatedListTest() {
        List<Object> list = new ArrayList<>();
        when(studentService.getUserPaginatedList(anyInt(), anyInt())).thenReturn(list);

        studentService.getUserPaginatedList(1, 2);

        verify(studentService).getUserPaginatedList(anyInt(), anyInt());
    }
}
