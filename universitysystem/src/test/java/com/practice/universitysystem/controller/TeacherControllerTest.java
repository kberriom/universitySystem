package com.practice.universitysystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.practice.universitysystem.dto.credentials.LoginCredentialsDto;
import com.practice.universitysystem.dto.users.TeacherDto;
import com.practice.universitysystem.model.users.UniversityUser;
import com.practice.universitysystem.model.users.teacher.Teacher;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Value("${test_admin_token}")
    private String adminSecret;

    private final ObjectMapper mapper = JsonMapper.builder()
            .addModule(new Jdk8Module())
            .addModule(new JavaTimeModule())
            .build();
    private final ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();

    private int generatedTeacherCount = 0;

    TeacherDto initTeacher() {
        generatedTeacherCount++;
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setName("testteacher " + generatedTeacherCount);
        teacherDto.setLastName("testteacher " + generatedTeacherCount + " lastname");
        teacherDto.setGovernmentId("1111111111111"+generatedTeacherCount);
        teacherDto.setEmail("testteacher"+ generatedTeacherCount +"@university.com");
        teacherDto.setMobilePhone("123456789"+generatedTeacherCount);
        teacherDto.setBirthdate(LocalDate.now());
        teacherDto.setUserPassword("123456."+generatedTeacherCount);
        teacherDto.setUsername("testteacher"+generatedTeacherCount);
        teacherDto.setDepartment("test department"+ (generatedTeacherCount%2));
        return teacherDto;
    }

    String getTeacherJWT(LoginCredentialsDto loginCredentials) throws Exception {
        String requestLoginJson=ow.writeValueAsString(loginCredentials);
        log.info(requestLoginJson);
        MvcResult resultJWT = this.mockMvc.perform(post("/auth/login")
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

    UniversityUser createTeacher(TeacherDto teacherDto) throws Exception {
        String requestJson=ow.writeValueAsString(teacherDto);
        log.info(requestJson);
        MvcResult result = this.mockMvc.perform(post("/auth/createTeacher")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret)
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is2xxSuccessful()).andReturn();
        log.info(result.getResponse().getContentAsString());
        return mapper.readValue(result.getResponse().getContentAsString(), Teacher.class);
    }

    @Test
    @Transactional
    void shouldGetTeacherInfo() throws Exception {
        TeacherDto teacherDto = initTeacher();
        UniversityUser user = createTeacher(teacherDto);
        LoginCredentialsDto loginCredentialsDto = new LoginCredentialsDto(user.getEmail(), teacherDto.getUserPassword());
        String JWT = getTeacherJWT(loginCredentialsDto);

        MvcResult result = this.mockMvc.perform(get("/teacher/getTeacherInfo")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT))
                .andExpect(status().is2xxSuccessful()).andReturn();
        log.info(result.getResponse().getContentAsString());
    }

    @Test
    @Transactional
    void shouldUpdateTeacherInfo() throws Exception {
        TeacherDto teacherDto = initTeacher();
        UniversityUser user = createTeacher(teacherDto);
        LoginCredentialsDto loginCredentialsDto = new LoginCredentialsDto(user.getEmail(), teacherDto.getUserPassword());
        String JWT = getTeacherJWT(loginCredentialsDto);

        teacherDto.setName(teacherDto.getName() + " Updated name");
        teacherDto.setLandPhone("156452235");
        teacherDto.setDepartment(teacherDto.getDepartment() + " Updated department");

        String requestLoginJson=ow.writeValueAsString(teacherDto);
        log.info(requestLoginJson);

        MvcResult result = this.mockMvc.perform(patch("/teacher/updateTeacherInfo")
                        .contentType(APPLICATION_JSON)
                        .content(requestLoginJson)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT))
                .andExpect(status().is2xxSuccessful()).andReturn();
        log.info(result.getResponse().getContentAsString());
    }

    @Test
    @Transactional
    void shouldDeleteTeacherInfo() throws Exception {
        TeacherDto teacherDto = initTeacher();
        UniversityUser user = createTeacher(teacherDto);
        LoginCredentialsDto loginCredentialsDto = new LoginCredentialsDto(user.getEmail(), teacherDto.getUserPassword());
        String JWT = getTeacherJWT(loginCredentialsDto);

        this.mockMvc.perform(delete("/teacher/deleteTeacherInfo")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + JWT))
                .andExpect(status().is2xxSuccessful()).andReturn();
    }

    @Test
    @Transactional
    void shouldGetTeacherInfoAdmin() throws Exception {
        TeacherDto teacherDto = initTeacher();
        UniversityUser user = createTeacher(teacherDto);

        MvcResult result = this.mockMvc.perform(get("/teacher/getTeacherInfo/{id}", user.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret))
                .andExpect(status().is2xxSuccessful()).andReturn();
        log.info(result.getResponse().getContentAsString());
    }

    @Test
    @Transactional
    void shouldGetAllTeacherInfoAdminAndPaged() throws Exception {
        TeacherDto teacherDto = initTeacher();
        UniversityUser user = createTeacher(teacherDto);

        TeacherDto teacherDto2 = initTeacher();
        UniversityUser user2 = createTeacher(teacherDto2);

        TeacherDto teacherDto3 = initTeacher();
        UniversityUser user3 = createTeacher(teacherDto3);

        MvcResult result = this.mockMvc.perform(get("/teacher/getAllTeacherInfo")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret))
                .andExpect(status().is2xxSuccessful()).andReturn();
        log.info(result.getResponse().getContentAsString());

        MvcResult resultPaged = this.mockMvc.perform(get("/teacher/getAllTeacherInfo/paged")
                        .param("page", "1")
                        .param("size", "2")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret))
                .andExpect(status().is2xxSuccessful()).andReturn();
        log.info(resultPaged.getResponse().getContentAsString());
    }

    @Test
    @Transactional
    void shouldUpdateTeacherInfoAdmin() throws Exception {
        TeacherDto teacherDto = initTeacher();
        UniversityUser user = createTeacher(teacherDto);

        teacherDto.setName(teacherDto.getName() + " Updated name");
        teacherDto.setLandPhone("156452235");
        teacherDto.setDepartment(teacherDto.getDepartment() + " Updated department");

        String requestLoginJson=ow.writeValueAsString(teacherDto);
        log.info(requestLoginJson);

        MvcResult result = this.mockMvc.perform(patch("/teacher/updateTeacherInfo/{id}", user.getId())
                        .contentType(APPLICATION_JSON)
                        .content(requestLoginJson)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret))
                .andExpect(status().is2xxSuccessful()).andReturn();
        log.info(result.getResponse().getContentAsString());
    }

    @Test
    @Transactional
    void shouldDeleteTeacherInfoAdmin() throws Exception {
        TeacherDto teacherDto = initTeacher();
        UniversityUser user = createTeacher(teacherDto);

        this.mockMvc.perform(delete("/teacher/deleteTeacherInfo/{id}", user.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret))
                .andExpect(status().is2xxSuccessful()).andReturn();
    }
}
