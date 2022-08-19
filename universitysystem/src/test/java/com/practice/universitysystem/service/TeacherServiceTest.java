package com.practice.universitysystem.service;

import com.practice.universitysystem.dto.users.TeacherDto;
import com.practice.universitysystem.model.users.teacher.Teacher;
import com.practice.universitysystem.service.users.teacher.TeacherService;
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
class TeacherServiceTest {

    @Mock
    TeacherService teacherService;

    @Test
    void createUserTest(@Mock TeacherDto teacherDto) {
        when(teacherService.createUser(any(TeacherDto.class))).thenReturn(any(Teacher.class));

        teacherService.createUser(teacherDto);

        verify(teacherService).createUser(teacherDto);
    }

    @Test
    void updateUserTest(@Mock TeacherDto teacherDto) {
        when(teacherService.updateUser(anyString(), any(TeacherDto.class))).thenReturn(any(Teacher.class));

        teacherService.updateUser(anyString(), teacherDto);

        verify(teacherService).updateUser(anyString(), any(TeacherDto.class));
    }

    @Test
    void getUserTest() {
        when(teacherService.getUser(anyString())).thenReturn(any(Teacher.class));

        teacherService.getUser("teacher@university.com");

        verify(teacherService).getUser(anyString());
    }

    @Test
    void getAllUsersTest() {
        List<Teacher> list = new ArrayList<>();
        when(teacherService.getAllUsers()).thenReturn(list);
        
        teacherService.getAllUsers();
        
        verify(teacherService).getAllUsers();
    }

    @Test
    void getUserPaginatedListTest() {
        List<Object> list = new ArrayList<>();
        when(teacherService.getUserPaginatedList(anyInt(), anyInt())).thenReturn(list);
        
        teacherService.getUserPaginatedList(1, 2);
        
        verify(teacherService).getUserPaginatedList(anyInt(), anyInt());
    }
}
