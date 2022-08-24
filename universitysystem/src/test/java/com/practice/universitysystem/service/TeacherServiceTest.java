package com.practice.universitysystem.service;

import com.practice.universitysystem.dto.users.TeacherDto;
import com.practice.universitysystem.model.users.teacher.Teacher;
import com.practice.universitysystem.repository.users.UniversityUserRepository;
import com.practice.universitysystem.repository.users.teacher.TeacherRepository;
import com.practice.universitysystem.service.users.teacher.TeacherService;
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
class TeacherServiceTest {
    @Mock
    AuthService authService;
    @Mock
    UniversityUserRepository userRepository;
    @Mock
    TeacherRepository instanceUserRepository;

    TeacherService teacherService;

    @BeforeEach
    void setup() {
        teacherService = new TeacherService(authService, userRepository, instanceUserRepository);
    }

    @Test
    void createUserTest() {
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setDepartment("test department");
        teacherDto.setName("testteacher");
        teacherDto.setLastName("testteacher lastname");
        teacherDto.setGovernmentId("1111111111111");
        teacherDto.setEmail("testteacher@university.com");
        teacherDto.setMobilePhone("123456789");
        teacherDto.setBirthdate(LocalDate.now());
        String rawPassword = "123456.";
        teacherDto.setUserPassword(rawPassword);
        teacherDto.setUsername("testteacher");
        when(authService.getEncodedPassword(teacherDto.getUserPassword())).then(invocationOnMock -> new BCryptPasswordEncoder().encode(invocationOnMock.getArgument(0)));
        when(instanceUserRepository.save(any(Teacher.class))).then(AdditionalAnswers.returnsFirstArg());

        assertThat(teacherService.createUser(teacherDto).getUserPassword(), is(not(rawPassword)));
    }

    @Test
    void updateUserTest() {
        Teacher teacher = new Teacher();
        teacher.setDepartment("department");
        teacher.setName("testteacher");
        teacher.setLastName("testteacher lastname");
        teacher.setGovernmentId("1111111111111");
        teacher.setEmail("testteacher@university.com");
        teacher.setMobilePhone("123456789");
        teacher.setBirthdate(LocalDate.now());
        teacher.setUserPassword("123456.");
        teacher.setUsername("testteacher");
        teacher.setEnrollmentDate(LocalDate.now());

        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setDepartment("test department mod");
        teacherDto.setName("testteacher mod");
        teacherDto.setLastName("testteacher lastname mod");
        teacherDto.setGovernmentId("bad");
        teacherDto.setEmail("bad");
        teacherDto.setMobilePhone("123456789");
        teacherDto.setLandPhone("123456789");
        teacherDto.setBirthdate(LocalDate.parse("1980-10-10"));
        String rawPassword = "bad";
        teacherDto.setUserPassword(rawPassword);
        teacherDto.setUsername("bad");

        when(instanceUserRepository.save(any(Teacher.class))).then(AdditionalAnswers.returnsFirstArg());
        when(instanceUserRepository.findById(anyLong())).thenReturn(Optional.of(teacher));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(teacher));

        Teacher updateUser = teacherService.updateUser(teacherDto.getEmail(), teacherDto);
        assertThat(updateUser.getEmail(), is(not(teacherDto.getEmail())));
        assertThat(updateUser.getDepartment(), is(teacherDto.getDepartment()));
        assertThat(updateUser.getBirthdate(), is(not(teacherDto.getBirthdate())));
        assertThat(updateUser.getUserPassword(), is(not(teacherDto.getUserPassword())));
        assertThat(updateUser.getUsername(), is(not(teacherDto.getUsername())));
        assertThat(updateUser.getName(), is(teacherDto.getName()));
        assertThat(updateUser.getLastName(), is(teacherDto.getLastName()));
        assertThat(updateUser.getMobilePhone(), is(teacherDto.getMobilePhone()));
        assertThat(updateUser.getLandPhone(), is(teacherDto.getLandPhone()));
    }

    @Test
    void updateUserAdminTest() {
        Teacher teacher = new Teacher();
        teacher.setId(1);
        teacher.setDepartment("department");
        teacher.setName("testteacher");
        teacher.setLastName("testteacher lastname");
        teacher.setGovernmentId("1111111111111");
        teacher.setEmail("testteacher@university.com");
        teacher.setMobilePhone("123456789");
        teacher.setBirthdate(LocalDate.now());
        teacher.setUserPassword("123456.");
        teacher.setUsername("testteacher");
        teacher.setEnrollmentDate(LocalDate.now());

        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setDepartment("test department mod");
        teacherDto.setName("testteacher mod");
        teacherDto.setLastName("testteacher lastname mod");
        teacherDto.setGovernmentId("update");
        teacherDto.setEmail("update@update.com");
        teacherDto.setMobilePhone("123456789");
        teacherDto.setLandPhone("123456789");
        teacherDto.setBirthdate(LocalDate.parse("1980-10-10"));
        String rawPassword = "update";
        teacherDto.setUserPassword(rawPassword);
        teacherDto.setUsername("update");

        when(instanceUserRepository.save(any(Teacher.class))).then(AdditionalAnswers.returnsFirstArg());
        when(instanceUserRepository.findById(anyLong())).thenReturn(Optional.of(teacher));

        Teacher updateUser = teacherService.updateUser(1, teacherDto);
        assertThat(updateUser.getEmail(), is(teacherDto.getEmail()));
        assertThat(updateUser.getDepartment(), is(teacherDto.getDepartment()));
        assertThat(updateUser.getBirthdate(), is(teacherDto.getBirthdate()));
        assertThat(updateUser.getUserPassword(), is(not(teacherDto.getUserPassword())));
        assertThat(updateUser.getUsername(), is(teacherDto.getUsername()));
        assertThat(updateUser.getName(), is(teacherDto.getName()));
        assertThat(updateUser.getLastName(), is(teacherDto.getLastName()));
        assertThat(updateUser.getMobilePhone(), is(teacherDto.getMobilePhone()));
        assertThat(updateUser.getLandPhone(), is(teacherDto.getLandPhone()));
    }

    @Test
    void getUserTest() {
        Teacher teacher = new Teacher();
        teacher.setId(1);
        teacher.setDepartment("department");
        teacher.setName("testteacher");
        teacher.setLastName("testteacher lastname");
        teacher.setGovernmentId("1111111111111");
        teacher.setEmail("testteacher@university.com");
        teacher.setMobilePhone("123456789");
        teacher.setBirthdate(LocalDate.now());
        teacher.setUserPassword("123456.");
        teacher.setUsername("testteacher");
        teacher.setEnrollmentDate(LocalDate.now());

        when(instanceUserRepository.findById(anyLong())).thenReturn(Optional.of(teacher));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(teacher));

        assertThat(teacherService.getUser(teacher.getEmail()), is(teacher));
    }

    @Test
    void getAllUsersTest() {
        Teacher teacher = mock(Teacher.class);
        when(instanceUserRepository.findAll()).thenReturn(List.of(mock(Teacher.class), teacher, mock(Teacher.class)));

        assertThat(teacherService.getAllUsers(), hasItem(teacher));
    }

    @Test
    void getUserPaginatedListTest() {
        Teacher teacher = mock(Teacher.class);
        Teacher teacher2 = mock(Teacher.class);
        ArrayList<Teacher> list = new ArrayList<>();
        list.add(teacher);
        list.add(teacher2);
        when(instanceUserRepository.findAll(isA(Pageable.class))).thenReturn(new PageImpl<>(list.stream().skip(list.size()-1).toList()));

        assertThat(teacherService.getUserPaginatedList(1,1).stream().skip(1).collect(Collectors.toList()),
                anyOf(hasItem(teacher), hasItem(teacher2)));
    }
}
