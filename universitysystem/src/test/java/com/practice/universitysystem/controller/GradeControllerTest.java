package com.practice.universitysystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.practice.universitysystem.dto.GradeDto;
import com.practice.universitysystem.dto.SubjectDto;
import com.practice.universitysystem.dto.credentials.LoginCredentialsDto;
import com.practice.universitysystem.dto.users.StudentDto;
import com.practice.universitysystem.model.curriculum.subject.Subject;
import com.practice.universitysystem.model.users.UniversityUser;
import com.practice.universitysystem.model.users.student.Student;
import com.practice.universitysystem.model.users.student.student_subject.StudentSubjectRegistration;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class GradeControllerTest {

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

    StudentDto initStudent() {
        StudentDto studentDto = new StudentDto();
        studentDto.setName("teststudent");
        studentDto.setLastName("teststudent lastname");
        studentDto.setGovernmentId("1111111111111");
        studentDto.setEmail("teststudent@university.com");
        studentDto.setMobilePhone("123456789");
        studentDto.setBirthdate(LocalDate.now());
        studentDto.setUserPassword("123456.");
        studentDto.setUsername("teststudent");
        return studentDto;
    }

    @Test
    @Transactional
    void shouldAddAndModStudentGradeThenDelete() throws Exception {
        SubjectDto subjectDto = initSubject();
        String requestJson=ow.writeValueAsString(subjectDto);
        log.info(requestJson);
        MvcResult resultSubject = this.mockMvc.perform(post("/subject/createSubject")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret)
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is2xxSuccessful()).andReturn();
        Subject subject = mapper.readValue(resultSubject.getResponse().getContentAsString(), Subject.class);

        StudentDto studentDto = initStudent();
        String requestJsonStudent =ow.writeValueAsString(studentDto);
        log.info(requestJsonStudent);
        MvcResult resultStudent = this.mockMvc.perform(post("/auth/createStudent")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret)
                        .contentType(APPLICATION_JSON)
                        .content(requestJsonStudent))
                .andExpect(status().is2xxSuccessful()).andReturn();
        UniversityUser student = mapper.readValue(resultStudent.getResponse().getContentAsString(), Student.class);

        MvcResult resultRegistration = this.mockMvc.perform(post("/subject/addStudent")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret)
                        .param("studentId", String.valueOf(student.getId()))
                        .param("subjectName", subject.getName()))
                .andExpect(status().is2xxSuccessful()).andReturn();
        log.info(resultRegistration.getResponse().getContentAsString());

        GradeDto gradeDto = new GradeDto();
        gradeDto.setDescription("Test grade");
        gradeDto.setGradeValue(5D);
        gradeDto.setPercentageOfFinalGrade(50D);
        String requestJsonGrade =ow.writeValueAsString(gradeDto);
        log.info(requestJsonGrade);

        MvcResult resultGrade = this.mockMvc.perform(post("/grade/addStudentGrade")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret)
                        .param("subjectId", String.valueOf(subject.getId()))
                        .param("studentId", String.valueOf(student.getId()))
                        .contentType(APPLICATION_JSON)
                        .content(requestJsonGrade))
                .andExpect(status().is2xxSuccessful()).andReturn();
        StudentSubjectRegistration subjectRegistration
                = mapper.readValue(resultGrade.getResponse().getContentAsString(), StudentSubjectRegistration.class);
        log.info(resultGrade.getResponse().getContentAsString());

        GradeDto gradeDtoMod = new GradeDto();
        gradeDtoMod.setDescription("Test grade mod");
        gradeDtoMod.setGradeValue(3D);
        String requestJsonGradeMod =ow.writeValueAsString(gradeDtoMod);
        log.info(requestJsonGradeMod);

        MvcResult resultMod = this.mockMvc.perform(patch("/grade/modifyStudentGrade")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret)
                        .param("subjectId", String.valueOf(subject.getId()))
                        .param("studentId", String.valueOf(student.getId()))
                        .param("gradeId", String.valueOf(subjectRegistration.getSubjectGrades().stream().findFirst().orElseThrow().getId()))
                        .contentType(APPLICATION_JSON)
                        .content(requestJsonGradeMod))
                .andExpect(status().is2xxSuccessful()).andReturn();
        log.info(resultMod.getResponse().getContentAsString());

        this.mockMvc.perform(delete("/grade/removeStudentGrade")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret)
                        .param("subjectId", String.valueOf(subject.getId()))
                        .param("studentId", String.valueOf(student.getId()))
                        .param("gradeId", String.valueOf(subjectRegistration.getSubjectGrades().stream().findFirst().orElseThrow().getId())))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @Transactional
    void shouldSetStudentFinalGradeAndAuto() throws Exception {
        SubjectDto subjectDto = initSubject();
        String requestJson=ow.writeValueAsString(subjectDto);
        log.info(requestJson);
        MvcResult resultSubject = this.mockMvc.perform(post("/subject/createSubject")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret)
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is2xxSuccessful()).andReturn();
        Subject subject = mapper.readValue(resultSubject.getResponse().getContentAsString(), Subject.class);

        StudentDto studentDto = initStudent();
        String requestJsonStudent =ow.writeValueAsString(studentDto);
        log.info(requestJsonStudent);
        MvcResult resultStudent = this.mockMvc.perform(post("/auth/createStudent")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret)
                        .contentType(APPLICATION_JSON)
                        .content(requestJsonStudent))
                .andExpect(status().is2xxSuccessful()).andReturn();
        UniversityUser student = mapper.readValue(resultStudent.getResponse().getContentAsString(), Student.class);

        MvcResult resultRegistration = this.mockMvc.perform(post("/subject/addStudent")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret)
                        .param("studentId", String.valueOf(student.getId()))
                        .param("subjectName", subject.getName()))
                .andExpect(status().is2xxSuccessful()).andReturn();
        log.info(resultRegistration.getResponse().getContentAsString());

        GradeDto gradeDto = new GradeDto();
        gradeDto.setDescription("Test grade");
        gradeDto.setGradeValue(2.5D);
        gradeDto.setPercentageOfFinalGrade(50D);
        String requestJsonGrade =ow.writeValueAsString(gradeDto);
        log.info(requestJsonGrade);

        GradeDto gradeDto2 = new GradeDto();
        gradeDto2.setDescription("Test grade 2");
        gradeDto2.setGradeValue(3.5D);
        gradeDto2.setPercentageOfFinalGrade(50D);
        String requestJsonGrade2 =ow.writeValueAsString(gradeDto2);
        log.info(requestJsonGrade2);

        MvcResult resultGrade = this.mockMvc.perform(post("/grade/addStudentGrade")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret)
                        .param("subjectId", String.valueOf(subject.getId()))
                        .param("studentId", String.valueOf(student.getId()))
                        .contentType(APPLICATION_JSON)
                        .content(requestJsonGrade))
                .andExpect(status().is2xxSuccessful()).andReturn();
        log.info(resultGrade.getResponse().getContentAsString());

        MvcResult resultGrade2 = this.mockMvc.perform(post("/grade/addStudentGrade")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret)
                        .param("subjectId", String.valueOf(subject.getId()))
                        .param("studentId", String.valueOf(student.getId()))
                        .contentType(APPLICATION_JSON)
                        .content(requestJsonGrade2))
                .andExpect(status().is2xxSuccessful()).andReturn();
        log.info(resultGrade2.getResponse().getContentAsString());

        MvcResult resultSetGradeAuto = this.mockMvc.perform(put("/grade/setStudentFinalGradeAuto")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret)
                        .param("subjectId", String.valueOf(subject.getId()))
                        .param("studentId", String.valueOf(student.getId())))
                .andExpect(status().is2xxSuccessful()).andReturn();
        log.info(resultSetGradeAuto.getResponse().getContentAsString());

        MvcResult resultSetGrade = this.mockMvc.perform(put("/grade/setStudentFinalGrade")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret)
                        .param("subjectId", String.valueOf(subject.getId()))
                        .param("studentId", String.valueOf(student.getId()))
                        .param("finalGrade", String.valueOf(5D)))
                .andExpect(status().is2xxSuccessful()).andReturn();
        log.info(resultSetGrade.getResponse().getContentAsString());
    }

    @Test
    @Transactional
    void shouldGetAllStudentAllGrades() throws Exception {
        SubjectDto subjectDto = initSubject();
        String requestJson=ow.writeValueAsString(subjectDto);
        log.info(requestJson);
        MvcResult resultSubject = this.mockMvc.perform(post("/subject/createSubject")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret)
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is2xxSuccessful()).andReturn();
        Subject subject = mapper.readValue(resultSubject.getResponse().getContentAsString(), Subject.class);

        StudentDto studentDto = initStudent();
        String requestJsonStudent =ow.writeValueAsString(studentDto);
        log.info(requestJsonStudent);
        MvcResult resultStudent = this.mockMvc.perform(post("/auth/createStudent")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret)
                        .contentType(APPLICATION_JSON)
                        .content(requestJsonStudent))
                .andExpect(status().is2xxSuccessful()).andReturn();
        UniversityUser student = mapper.readValue(resultStudent.getResponse().getContentAsString(), Student.class);

        MvcResult resultRegistration = this.mockMvc.perform(post("/subject/addStudent")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret)
                        .param("studentId", String.valueOf(student.getId()))
                        .param("subjectName", subject.getName()))
                .andExpect(status().is2xxSuccessful()).andReturn();
        log.info(resultRegistration.getResponse().getContentAsString());

        GradeDto gradeDto = new GradeDto();
        gradeDto.setDescription("Test grade");
        gradeDto.setGradeValue(5D);
        gradeDto.setPercentageOfFinalGrade(50D);
        String requestJsonGrade =ow.writeValueAsString(gradeDto);
        log.info(requestJsonGrade);

        MvcResult resultGrade = this.mockMvc.perform(post("/grade/addStudentGrade")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret)
                        .param("subjectId", String.valueOf(subject.getId()))
                        .param("studentId", String.valueOf(student.getId()))
                        .contentType(APPLICATION_JSON)
                        .content(requestJsonGrade))
                .andExpect(status().is2xxSuccessful()).andReturn();
        log.info(resultGrade.getResponse().getContentAsString());


        MvcResult resultAllGradesInSubject = this.mockMvc.perform(get("/grade/getAllStudentAllGradesInSubject")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret)
                        .param("subjectId", String.valueOf(subject.getId())))
                .andExpect(status().is2xxSuccessful()).andReturn();
        log.info(resultAllGradesInSubject.getResponse().getContentAsString());


        MvcResult resultAllGradesAllSubjects = this.mockMvc.perform(get("/grade/getAllStudentAllGrades")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret))
                .andExpect(status().is2xxSuccessful()).andReturn();
        log.info(resultAllGradesAllSubjects.getResponse().getContentAsString());


        MvcResult resultOneStudentGradesInSubject = this.mockMvc.perform(get("/grade/getOneStudentGrades")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret)
                        .param("subjectId", String.valueOf(subject.getId()))
                        .param("studentId", String.valueOf(student.getId())))
                .andExpect(status().is2xxSuccessful()).andReturn();
        log.info(resultOneStudentGradesInSubject.getResponse().getContentAsString());


        LoginCredentialsDto credentials =
                new LoginCredentialsDto(student.getEmail(), studentDto.getUserPassword());
        String requestLoginJson=ow.writeValueAsString(credentials);
        log.info(requestLoginJson);

        MvcResult resultStudentJWT = this.mockMvc.perform(get("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(requestLoginJson))
                .andExpect(status().is2xxSuccessful()).andReturn();

        String rawResponse = resultStudentJWT.getResponse().getContentAsString();
        log.info("Student JWT raw response = " + rawResponse);
        String extractedJWT = rawResponse.
                substring(rawResponse.substring(0, rawResponse.indexOf(":")).length()+2, rawResponse.lastIndexOf('"'));
        log.info("Student extracted JWT = " + extractedJWT);

        MvcResult resultOneStudentGradesInSubjectStudentAuth = this.mockMvc.perform(
                get("/grade/getMyGrade/{subjectName}", subject.getName())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + extractedJWT))
                .andExpect(status().is2xxSuccessful()).andReturn();
        log.info(resultOneStudentGradesInSubjectStudentAuth.getResponse().getContentAsString());
    }
}
