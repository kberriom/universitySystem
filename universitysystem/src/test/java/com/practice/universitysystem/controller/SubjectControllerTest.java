package com.practice.universitysystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.practice.universitysystem.dto.SubjectDto;
import com.practice.universitysystem.dto.users.StudentDto;
import com.practice.universitysystem.dto.users.TeacherDto;
import com.practice.universitysystem.model.users.UniversityUser;
import com.practice.universitysystem.model.users.student.Student;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class SubjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Value("${test_admin_token}")
    private String adminSecret;

    private final ObjectMapper mapper = JsonMapper.builder()
            .addModule(new Jdk8Module())
            .addModule(new JavaTimeModule())
            .build();
    private final ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();

    SubjectDto initSubject() {
        SubjectDto subjectDto = new SubjectDto();
        subjectDto.setName("test subject");
        subjectDto.setDescription("test description");
        subjectDto.setStartDate(LocalDate.parse("2026-05-14"));
        subjectDto.setEndDate(LocalDate.parse("2027-05-14"));
        subjectDto.setRemote(true);
        subjectDto.setOnSite(true);
        subjectDto.setRoomLocation("18-302");
        subjectDto.setCreditsValue(26);

        return subjectDto;
    }

    @Test
    @Transactional
    void shouldCreateSubject() throws Exception {
        SubjectDto subjectDto = initSubject();
        String requestJson=ow.writeValueAsString(subjectDto);
        log.info(requestJson);

        this.mockMvc.perform(post("/subject/createSubject")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret)
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @Transactional
    void shouldGetSubject() throws Exception {
        SubjectDto subjectDto = initSubject();
        String requestJson=ow.writeValueAsString(subjectDto);
        log.info(requestJson);

        this.mockMvc.perform(post("/subject/createSubject")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret)
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is2xxSuccessful());

        MvcResult result = this.mockMvc.perform(get("/subject/getSubject")
                        .contentType(APPLICATION_JSON)
                        .param("name", subjectDto.getName()))
                .andExpect(status().is2xxSuccessful()).andReturn();

        log.info(result.getResponse().getContentAsString());
    }

    @Test
    @Transactional
    void shouldGetAllSubjectsAndPaged() throws Exception {
        SubjectDto subjectDto = initSubject();

        SubjectDto subjectDto2 = new SubjectDto();
        subjectDto2.setName("test subject 2");
        subjectDto2.setDescription("test description");
        subjectDto2.setStartDate(LocalDate.parse("2026-05-14"));
        subjectDto2.setEndDate(LocalDate.parse("2027-05-14"));
        subjectDto2.setRemote(true);
        subjectDto2.setOnSite(true);
        subjectDto2.setRoomLocation("18-203");
        subjectDto2.setCreditsValue(20);

        String requestJson=ow.writeValueAsString(subjectDto);
        String requestJson2=ow.writeValueAsString(subjectDto2);

        log.info(requestJson);
        log.info(requestJson2);

        this.mockMvc.perform(post("/subject/createSubject")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret)
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is2xxSuccessful());
        this.mockMvc.perform(post("/subject/createSubject")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret)
                        .contentType(APPLICATION_JSON)
                        .content(requestJson2))
                .andExpect(status().is2xxSuccessful());

        MvcResult result = this.mockMvc.perform(get("/subject/getAllSubjects"))
                .andExpect(status().is2xxSuccessful()).andReturn();

        log.info(result.getResponse().getContentAsString());

        MvcResult resultPaged = this.mockMvc.perform(get("/subject/getAllSubjects/paged")
                        .param("page", "1")
                        .param("size", "1"))
                .andExpect(status().is2xxSuccessful()).andReturn();

        log.info(resultPaged.getResponse().getContentAsString());
    }

    @Test
    @Transactional
    void shouldUpdateSubject() throws Exception {
        SubjectDto subjectDto = initSubject();
        String requestJson=ow.writeValueAsString(subjectDto);
        log.info(requestJson);

        MvcResult result = this.mockMvc.perform(post("/subject/createSubject")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret)
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is2xxSuccessful()).andReturn();
        log.info(result.getResponse().getContentAsString());

        SubjectDto subjectDtoUpdate = new SubjectDto();
        subjectDtoUpdate.setName("test subject updated");
        subjectDtoUpdate.setDescription("test description updated");

        String requestJsonUpdated =ow.writeValueAsString(subjectDtoUpdate);

        log.info(requestJsonUpdated);

        MvcResult resultUpdated = this.mockMvc.perform(patch("/subject/updateSubject/{name}", subjectDto.getName())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret)
                        .contentType(APPLICATION_JSON)
                        .content(requestJsonUpdated))
                .andExpect(status().is2xxSuccessful()).andReturn();
        log.info(resultUpdated.getResponse().getContentAsString());
    }

    @Test
    @Transactional
    void shouldDeleteSubject() throws Exception {
        SubjectDto subjectDto = initSubject();
        String requestJson=ow.writeValueAsString(subjectDto);
        log.info(requestJson);

        this.mockMvc.perform(post("/subject/createSubject")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret)
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is2xxSuccessful());

        this.mockMvc.perform(delete("/subject/deleteSubject")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret)
                        .param("name", subjectDto.getName()))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @Transactional
    void shouldGetAllRegisteredStudents() throws Exception {
        SubjectDto subjectDto = initSubject();
        String requestJson=ow.writeValueAsString(subjectDto);
        log.info(requestJson);

        this.mockMvc.perform(post("/subject/createSubject")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret)
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is2xxSuccessful());


        MvcResult result = this.mockMvc.perform(get("/subject/getAllRegisteredStudents/{subjectName}", subjectDto.getName())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret)
                        .contentType(APPLICATION_JSON)
                        .param("name", subjectDto.getName()))
                .andExpect(status().is2xxSuccessful()).andReturn();

        log.info(result.getResponse().getContentAsString());
    }

    @Test
    @Transactional
    void shouldAddAndRemoveStudent() throws Exception {
        SubjectDto subjectDto = initSubject();
        String requestJson=ow.writeValueAsString(subjectDto);
        log.info(requestJson);

        this.mockMvc.perform(post("/subject/createSubject")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret)
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is2xxSuccessful());

        StudentDto studentDto = new StudentDto();
        studentDto.setName("teststudent");
        studentDto.setLastName("teststudent lastname");
        studentDto.setGovernmentId("1111111111111");
        studentDto.setEmail("teststudent@university.com");
        studentDto.setMobilePhone("123456789");
        studentDto.setBirthdate(LocalDate.now());
        studentDto.setUserPassword("123456.");
        studentDto.setUsername("teststudent");

        String requestJsonStudent =ow.writeValueAsString(studentDto);

        log.info(requestJsonStudent);

        MvcResult result = this.mockMvc.perform(post("/auth/createStudent")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret)
                        .contentType(APPLICATION_JSON)
                        .content(requestJsonStudent))
                .andExpect(status().is2xxSuccessful()).andReturn();

        log.info(result.getResponse().getContentAsString());

        UniversityUser student = mapper.readValue(result.getResponse().getContentAsString(), Student.class);

        MvcResult resultRegistration = this.mockMvc.perform(post("/subject/addStudent")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret)
                        .param("studentId", String.valueOf(student.getId()))
                        .param("subjectName", subjectDto.getName()))
                .andExpect(status().is2xxSuccessful()).andReturn();
        log.info(resultRegistration.getResponse().getContentAsString());

        this.mockMvc.perform(delete("/subject/removeStudent")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret)
                        .param("studentId", String.valueOf(student.getId()))
                        .param("subjectName", subjectDto.getName()))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @Transactional
    void shouldAddThenGetAllAndDeleteTeacher() throws Exception {
        SubjectDto subjectDto = initSubject();
        String requestJson=ow.writeValueAsString(subjectDto);
        log.info(requestJson);

        this.mockMvc.perform(post("/subject/createSubject")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret)
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is2xxSuccessful());

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

        String requestJsonTeacher =ow.writeValueAsString(teacherDto);

        MvcResult resultCreateTeacher = this.mockMvc.perform(post("/auth/createTeacher")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret)
                        .contentType(APPLICATION_JSON)
                        .content(requestJsonTeacher))
                .andExpect(status().is2xxSuccessful()).andReturn();

        UniversityUser teacher = mapper.readValue(resultCreateTeacher.getResponse().getContentAsString(), Teacher.class);

        MvcResult result = this.mockMvc.perform(put("/subject/addTeacher")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret)
                        .param("subjectName", subjectDto.getName())
                        .param("teacherId", String.valueOf(teacher.getId()))
                        .param("roleInClass", "Main teacher"))
                .andExpect(status().is2xxSuccessful()).andReturn();
        log.info(result.getResponse().getContentAsString());

        MvcResult resultGetAll = this.mockMvc.perform(get("/subject/getAllTeachers/{subjectName}", subjectDto.getName())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret))
                .andExpect(status().is2xxSuccessful()).andReturn();
        log.info(resultGetAll.getResponse().getContentAsString());

        MvcResult resultMod = this.mockMvc.perform(post("/subject/modifyTeacherRole")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret)
                        .param("subjectName", subjectDto.getName())
                        .param("teacherId", String.valueOf(teacher.getId()))
                        .param("roleInClass", "Temporal teacher"))
                .andExpect(status().is2xxSuccessful()).andReturn();
        log.info(resultMod.getResponse().getContentAsString());

        this.mockMvc.perform(delete("/subject/removeTeacher")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret)
                        .param("subjectName", subjectDto.getName())
                        .param("teacherId", String.valueOf(teacher.getId())))
                .andExpect(status().is2xxSuccessful());
    }
}
