package com.practice.universitysystem.service;

import com.practice.universitysystem.dto.CurriculumDto;
import com.practice.universitysystem.model.curriculum.Curriculum;
import com.practice.universitysystem.model.curriculum.subject.Subject;
import com.practice.universitysystem.repository.curriculum.CurriculumRepository;
import com.practice.universitysystem.repository.curriculum.subject.SubjectRepository;
import com.practice.universitysystem.service.curriculum.CurriculumService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class CurriculumServiceTest {

    @Mock
    CurriculumRepository curriculumRepository;
    @Mock
    SubjectRepository subjectRepository;

    CurriculumService curriculumService;

    @BeforeEach
    void setup() {
        curriculumService = new CurriculumService(curriculumRepository, subjectRepository);
    }

    private CurriculumDto initCurriculum() {
        CurriculumDto curriculumDto = new CurriculumDto();
        curriculumDto.setName("test curriculum");
        curriculumDto.setDescription("test description ");
        curriculumDto.setDateStart(LocalDate.parse("2026-05-05"));
        curriculumDto.setDateEnd(LocalDate.parse("2027-06-06"));
        return curriculumDto;
    }

    @Test
    void createCurriculum() {
        when(curriculumRepository.save(any(Curriculum.class))).then(returnsFirstArg());

        assertThat(curriculumService.createCurriculum(initCurriculum()), is(instanceOf(Curriculum.class)));
    }

    @Test
    void getCurriculumTest() {
        when(curriculumRepository.findByName(anyString())).thenReturn(Optional.of(new Curriculum()));

        assertThat(curriculumService.getCurriculum("Curriculum"), is(instanceOf(Curriculum.class)));
    }

    @Test
    void getAllCurriculumTest() {
        Subject subject = mock(Subject.class);

        Curriculum curriculum = new Curriculum();
        curriculum.setId(1);
        curriculum.setName("test curriculum");
        curriculum.setDescription("test description ");
        curriculum.setDateStart(LocalDate.parse("2026-05-05"));
        curriculum.setDateEnd(LocalDate.parse("2027-06-06"));
        curriculum.setSubjects(new HashSet<>());
        curriculum.getSubjects().add(subject);

        when(curriculumRepository.findAll()).thenReturn(List.of(curriculum));
        assertThat(curriculumService.getAllCurriculum(), containsInAnyOrder(curriculum));
    }

    @Test
    void getPaginatedCurriculumTest() {
        Subject subject = mock(Subject.class);

        Curriculum curriculum = new Curriculum();
        curriculum.setId(1);
        curriculum.setName("test curriculum");
        curriculum.setDescription("test description ");
        curriculum.setDateStart(LocalDate.parse("2026-05-05"));
        curriculum.setDateEnd(LocalDate.parse("2027-06-06"));
        curriculum.setSubjects(new HashSet<>());
        curriculum.getSubjects().add(subject);

        ArrayList<Curriculum> list = new ArrayList<>();
        list.add(curriculum);
        when(curriculumRepository.findAll(isA(Pageable.class))).thenReturn(new PageImpl<>(list));
        assertThat(curriculumService.getPaginatedCurriculum(1,1).get(1), is(curriculum));
    }

    @Test
    void updateCurriculumTest() {
        when(curriculumRepository.save(any(Curriculum.class))).then(returnsFirstArg());
        when(curriculumRepository.findByName(anyString())).thenReturn(Optional.of(new Curriculum()));

        assertThat(curriculumService.updateCurriculum("Name", initCurriculum()), is(instanceOf(Curriculum.class)));
    }

    @Test
    void addSubjectTest() {
        Subject subject = new Subject();
        subject.setName("test subject");
        subject.setDescription("test description");
        subject.setStartDate(LocalDate.parse("2026-05-15"));
        subject.setEndDate(LocalDate.parse("2027-06-16"));
        subject.setRemote(true);
        subject.setOnSite(true);
        subject.setRoomLocation("18-30");
        subject.setCreditsValue(3);
        
        Curriculum curriculum = new Curriculum();
        curriculum.setId(1);
        curriculum.setName("test curriculum");
        curriculum.setDescription("test description ");
        curriculum.setDateStart(LocalDate.parse("2026-05-05"));
        curriculum.setDateEnd(LocalDate.parse("2027-06-06"));
        
        when(curriculumRepository.findByName(curriculum.getName())).thenReturn(Optional.of(curriculum));
        when(subjectRepository.findByName(subject.getName())).thenReturn(Optional.of(subject));
        when(curriculumRepository.save(any(Curriculum.class))).then(returnsFirstArg());

        assertThat(curriculumService.addSubject("test curriculum","test subject").get(1), is(subject));
    }

    @Test
    void getAllSubjectsInCurriculum() {
        Subject subject = mock(Subject.class);

        Curriculum curriculum = new Curriculum();
        curriculum.setId(1);
        curriculum.setName("test curriculum");
        curriculum.setDescription("test description ");
        curriculum.setDateStart(LocalDate.parse("2026-05-05"));
        curriculum.setDateEnd(LocalDate.parse("2027-06-06"));
        curriculum.setSubjects(new HashSet<>());
        curriculum.getSubjects().add(subject);

        when(curriculumRepository.findByName(curriculum.getName())).thenReturn(Optional.of(curriculum));

        assertThat(curriculumService.getAllSubjectsInCurriculum(curriculum.getName()), is(Set.of(subject)));
    }
}
