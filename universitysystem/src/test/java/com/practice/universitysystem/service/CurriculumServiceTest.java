package com.practice.universitysystem.service;

import com.practice.universitysystem.dto.CurriculumDto;
import com.practice.universitysystem.model.curriculum.Curriculum;
import com.practice.universitysystem.model.curriculum.subject.Subject;
import com.practice.universitysystem.service.curriculum.CurriculumService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@Slf4j
@ExtendWith(MockitoExtension.class)
class CurriculumServiceTest {

    @Mock
    CurriculumService curriculumService;

    @Test
    void createCurriculum(@Mock CurriculumDto curriculumDto) {
        when(curriculumService.createCurriculum(any(CurriculumDto.class))).thenReturn(any(Curriculum.class));

        curriculumService.createCurriculum(curriculumDto);

        verify(curriculumService).createCurriculum(any(CurriculumDto.class));
    }

    @Test
    void getCurriculumTest() {
        when(curriculumService.getCurriculum(anyString())).thenReturn(any(Curriculum.class));

        curriculumService.getCurriculum("name");

        verify(curriculumService).getCurriculum(anyString());
    }

    @Test
    void getAllCurriculumTest() {
        List<Curriculum> list = new ArrayList<>();
        when(curriculumService.getAllCurriculum()).thenReturn(list);

        curriculumService.getAllCurriculum();

        verify(curriculumService).getAllCurriculum();
    }

    @Test
    void getPaginatedCurriculumTest() {
        List<Object> list = new ArrayList<>();
        when(curriculumService.getPaginatedCurriculum(anyInt(), anyInt())).thenReturn(list);

        curriculumService.getPaginatedCurriculum(1, 2);

        verify(curriculumService).getPaginatedCurriculum(anyInt(), anyInt());
    }

    @Test
    void updateCurriculumTest(@Mock CurriculumDto curriculumDto) {
        when(curriculumService.updateCurriculum(anyString(), any(CurriculumDto.class))).thenReturn(any(Curriculum.class));

        curriculumService.updateCurriculum(anyString(), curriculumDto);

        verify(curriculumService).updateCurriculum(anyString(), any(CurriculumDto.class));
    }

    @Test
    void addSubjectTest() {
        List<Object> list = new ArrayList<>();
        when(curriculumService.addSubject(anyString(), anyString())).thenReturn(list);

        curriculumService.addSubject(anyString(), anyString());

        verify(curriculumService).addSubject(anyString(), anyString());
    }

    @Test
    void getAllSubjectsInCurriculum() {
        Set<Subject> list = new HashSet<>();
        when(curriculumService.getAllSubjectsInCurriculum(anyString())).thenReturn(list);

        curriculumService.getAllSubjectsInCurriculum(anyString());

        verify(curriculumService).getAllSubjectsInCurriculum(anyString());
    }
}
