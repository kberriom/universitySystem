package com.practice.universitysystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.practice.universitysystem.dto.credentials.LoginCredentialsDto;
import com.practice.universitysystem.dto.credentials.NewPasswordDto;
import com.practice.universitysystem.dto.users.StudentDto;
import com.practice.universitysystem.dto.users.TeacherDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Value("${test_admin_token}")
    private String adminSecret;

    private final ObjectMapper mapper = JsonMapper.builder()
            .addModule(new Jdk8Module())
            .addModule(new JavaTimeModule())
            .build();
    private final ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();

    @Test
    @Transactional
    void shouldCreateStudent() throws Exception {
        StudentDto studentDto = new StudentDto();
        studentDto.setName("teststudent");
        studentDto.setLastName("teststudent lastname");
        studentDto.setGovernmentId("1111111111111");
        studentDto.setEmail("teststudent@university.com");
        studentDto.setMobilePhone("123456789");
        studentDto.setBirthdate(LocalDate.now());
        studentDto.setUserPassword("123456.");
        studentDto.setUsername("teststudent");

        String requestJson=ow.writeValueAsString(studentDto);

        log.info(requestJson);

        this.mockMvc.perform(post("/auth/createStudent")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret)
                .contentType(APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @Transactional
    void shouldCreateTeacher() throws Exception {
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setName("testteacher");
        teacherDto.setLastName("testteacher lastname");
        teacherDto.setGovernmentId("1111111111111");
        teacherDto.setEmail("testteacher@university.com");
        teacherDto.setMobilePhone("123456789");
        teacherDto.setBirthdate(LocalDate.now());
        teacherDto.setUserPassword("123456.");
        teacherDto.setUsername("testteacher");
        teacherDto.setDepartment("test department");

        String requestJson=ow.writeValueAsString(teacherDto);

        log.info(requestJson);

        this.mockMvc.perform(post("/auth/createTeacher")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret)
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @Transactional
    void shouldCreateJWT() throws Exception {

        StudentDto studentDto = new StudentDto();
        studentDto.setName("teststudent");
        studentDto.setLastName("teststudent lastname");
        studentDto.setGovernmentId("1111111111111");
        studentDto.setEmail("teststudent@university.com");
        studentDto.setMobilePhone("123456789");
        studentDto.setBirthdate(LocalDate.now());
        studentDto.setUserPassword("123456.");
        studentDto.setUsername("teststudent");

        String requestJson=ow.writeValueAsString(studentDto);

        log.info(requestJson);

        this.mockMvc.perform(post("/auth/createStudent")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret)
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is2xxSuccessful());

        LoginCredentialsDto credentials =
                new LoginCredentialsDto(studentDto.getEmail(), studentDto.getUserPassword());

        String requestLoginJson=ow.writeValueAsString(credentials);

        log.info(requestLoginJson);

        this.mockMvc.perform(get("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(requestLoginJson))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @Transactional
    void shouldChangePassword() throws Exception {

        StudentDto studentDto = new StudentDto();
        studentDto.setName("teststudent");
        studentDto.setLastName("teststudent lastname");
        studentDto.setGovernmentId("1111111111111");
        studentDto.setEmail("teststudent@university.com");
        studentDto.setMobilePhone("123456789");
        studentDto.setBirthdate(LocalDate.now());
        studentDto.setUserPassword("123456.");
        studentDto.setUsername("teststudent");

        String requestJson=ow.writeValueAsString(studentDto);

        log.info(requestJson);

        this.mockMvc.perform(post("/auth/createStudent")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret)
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is2xxSuccessful());

        LoginCredentialsDto credentials =
                new LoginCredentialsDto(studentDto.getEmail(), studentDto.getUserPassword());

        String requestLoginJson=ow.writeValueAsString(credentials);

        log.info(requestLoginJson);

        MvcResult result = this.mockMvc.perform(get("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(requestLoginJson))
                .andExpect(status().is2xxSuccessful()).andReturn();

        log.info(result.getResponse().getContentAsString());

        NewPasswordDto passwordDto = new NewPasswordDto
                (studentDto.getEmail(), studentDto.getUserPassword(), studentDto.getUserPassword() + "123");

        String requestPasswordJson=ow.writeValueAsString(passwordDto);

        log.info(requestPasswordJson);

        this.mockMvc.perform(post("/auth/updatePassword")
                        //.header(HttpHeaders.AUTHORIZATION, "Bearer " + )
                        .contentType(APPLICATION_JSON)
                        .content(requestPasswordJson))
                .andExpect(status().is2xxSuccessful());
    }
}
