package com.practice.universitysystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.practice.universitysystem.dto.credentials.LoginCredentialsDto;
import com.practice.universitysystem.dto.users.StudentDto;
import com.practice.universitysystem.model.users.UniversityUser;
import com.practice.universitysystem.model.users.student.Student;
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
import java.util.Random;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Value("${test_admin_token}")
    private String adminSecret;

    private final ObjectMapper mapper = JsonMapper.builder()
            .addModule(new Jdk8Module())
            .addModule(new JavaTimeModule())
            .build();
    private final ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();

    StudentDto initStudent() {
        int generatedStudentCount = new Random().nextInt();
        StudentDto studentDto = new StudentDto();
        studentDto.setName("teststudent " + generatedStudentCount);
        studentDto.setLastName("teststudent " + generatedStudentCount + " lastname");
        studentDto.setGovernmentId("1111111111111"+generatedStudentCount);
        studentDto.setEmail("teststudent"+ generatedStudentCount +"@university.com");
        studentDto.setMobilePhone("123456789"+generatedStudentCount);
        studentDto.setBirthdate(LocalDate.now());
        studentDto.setUserPassword("123456."+generatedStudentCount);
        studentDto.setUsername("teststudent"+generatedStudentCount);
        return studentDto;
    }

    String getStudentJWT(LoginCredentialsDto loginCredentials) throws Exception {
        String requestLoginJson=ow.writeValueAsString(loginCredentials);
        log.info(requestLoginJson);
        MvcResult resultJWT = this.mockMvc.perform(get("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(requestLoginJson))
                .andExpect(status().is2xxSuccessful()).andReturn();

        String rawResponse = resultJWT.getResponse().getContentAsString();
        log.info("JWT raw response = " + rawResponse);
        String extractedJWT = rawResponse.
                substring(rawResponse.substring(0, rawResponse.indexOf(":")).length()+2, rawResponse.lastIndexOf('"'));
        log.info("Extracted JWT = " + extractedJWT);
        return extractedJWT;
    }

    UniversityUser createStudent(StudentDto studentDto) throws Exception {
        String requestJson=ow.writeValueAsString(studentDto);
        log.info(requestJson);
        MvcResult result = this.mockMvc.perform(post("/auth/createStudent")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret)
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is2xxSuccessful()).andReturn();
        log.info(result.getResponse().getContentAsString());
        return mapper.readValue(result.getResponse().getContentAsString(), Student.class);
    }

    @Test
    @Transactional
    void shouldGetStudentInfo() throws Exception {
        StudentDto studentDto = initStudent();
        UniversityUser user = createStudent(studentDto);
        LoginCredentialsDto loginCredentialsDto = new LoginCredentialsDto(user.getEmail(), studentDto.getUserPassword());
        String JWT = getStudentJWT(loginCredentialsDto);

        MvcResult result = this.mockMvc.perform(get("/student/getStudentInfo")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT))
                .andExpect(status().is2xxSuccessful()).andReturn();
        log.info(result.getResponse().getContentAsString());
    }

    @Test
    @Transactional
    void shouldUpdateStudentInfo() throws Exception {
        StudentDto studentDto = initStudent();
        UniversityUser user = createStudent(studentDto);
        LoginCredentialsDto loginCredentialsDto = new LoginCredentialsDto(user.getEmail(), studentDto.getUserPassword());
        String JWT = getStudentJWT(loginCredentialsDto);

        studentDto.setName(studentDto.getName() + " Updated name");
        studentDto.setLandPhone("156452235");

        String requestLoginJson=ow.writeValueAsString(studentDto);
        log.info(requestLoginJson);

        MvcResult result = this.mockMvc.perform(patch("/student/updateStudentInfo")
                        .contentType(APPLICATION_JSON)
                        .content(requestLoginJson)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT))
                .andExpect(status().is2xxSuccessful()).andReturn();
        log.info(result.getResponse().getContentAsString());
    }

    @Test
    @Transactional
    void shouldDeleteStudentInfo() throws Exception {
        StudentDto studentDto = initStudent();
        UniversityUser user = createStudent(studentDto);
        LoginCredentialsDto loginCredentialsDto = new LoginCredentialsDto(user.getEmail(), studentDto.getUserPassword());
        String JWT = getStudentJWT(loginCredentialsDto);

        this.mockMvc.perform(delete("/student/deleteStudentInfo")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT))
                .andExpect(status().is2xxSuccessful()).andReturn();
    }

    @Test
    @Transactional
    void shouldGetStudentInfoAdmin() throws Exception {
        StudentDto studentDto = initStudent();
        UniversityUser user = createStudent(studentDto);

        MvcResult result = this.mockMvc.perform(get("/student/getStudentInfo/{id}", user.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret))
                .andExpect(status().is2xxSuccessful()).andReturn();
        log.info(result.getResponse().getContentAsString());
    }

    @Test
    @Transactional
    void shouldGetAllStudentInfoAdminAndPaged() throws Exception {
        StudentDto studentDto = initStudent();
        UniversityUser user = createStudent(studentDto);

        StudentDto studentDto2 = initStudent();
        UniversityUser user2 = createStudent(studentDto2);

        StudentDto studentDto3 = initStudent();
        UniversityUser user3 = createStudent(studentDto3);

        MvcResult result = this.mockMvc.perform(get("/student/getAllStudentInfo")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret))
                .andExpect(status().is2xxSuccessful()).andReturn();
        log.info(result.getResponse().getContentAsString());

        MvcResult resultPaged = this.mockMvc.perform(get("/student/getAllStudentInfo/paged")
                        .param("page", "1")
                        .param("size", "2")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret))
                .andExpect(status().is2xxSuccessful()).andReturn();
        log.info(resultPaged.getResponse().getContentAsString());
    }

    @Test
    @Transactional
    void shouldUpdateStudentInfoAdmin() throws Exception {
        StudentDto studentDto = initStudent();
        UniversityUser user = createStudent(studentDto);

        studentDto.setName(studentDto.getName() + " Updated name");
        studentDto.setLandPhone("156452235");

        String requestLoginJson=ow.writeValueAsString(studentDto);
        log.info(requestLoginJson);

        MvcResult result = this.mockMvc.perform(patch("/student/updateStudentInfo/{id}", user.getId())
                        .contentType(APPLICATION_JSON)
                        .content(requestLoginJson)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret))
                .andExpect(status().is2xxSuccessful()).andReturn();
        log.info(result.getResponse().getContentAsString());
    }

    @Test
    @Transactional
    void shouldDeleteStudentInfoAdmin() throws Exception {
        StudentDto studentDto = initStudent();
        UniversityUser user = createStudent(studentDto);

        this.mockMvc.perform(delete("/student/deleteStudentInfo/{id}", user.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret))
                .andExpect(status().is2xxSuccessful()).andReturn();
    }
}
