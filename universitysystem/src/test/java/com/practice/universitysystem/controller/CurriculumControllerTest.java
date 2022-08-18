package com.practice.universitysystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.practice.universitysystem.dto.CurriculumDto;
import com.practice.universitysystem.dto.SubjectDto;
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
class CurriculumControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Value("${test_admin_token}")
    private String adminSecret;

    private final ObjectMapper mapper = JsonMapper.builder()
            .addModule(new Jdk8Module())
            .addModule(new JavaTimeModule())
            .build();
    private final ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();

    CurriculumDto initCurriculum() {
        CurriculumDto curriculumDto = new CurriculumDto();
        curriculumDto.setName("test curriculum");
        curriculumDto.setDescription("test description");
        curriculumDto.setDateStart(LocalDate.parse("2026-05-14"));
        curriculumDto.setDateEnd(LocalDate.parse("2027-05-14"));
        return curriculumDto;
    }

    @Test
    @Transactional
    void shouldCreateCurriculum() throws Exception {
        CurriculumDto curriculumDto = initCurriculum();
        String requestJson=ow.writeValueAsString(curriculumDto);
        log.info(requestJson);

        this.mockMvc.perform(post("/curriculum/createCurriculum")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret)
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @Transactional
    void shouldDeleteCurriculum() throws Exception {
        CurriculumDto curriculumDto = initCurriculum();
        String requestJson=ow.writeValueAsString(curriculumDto);
        log.info(requestJson);

        this.mockMvc.perform(post("/curriculum/createCurriculum")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret)
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is2xxSuccessful());


        this.mockMvc.perform(delete("/curriculum/deleteCurriculum")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret)
                        .contentType(APPLICATION_JSON)
                .param("name", curriculumDto.getName()))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @Transactional
    void shouldGetCurriculum() throws Exception {
        CurriculumDto curriculumDto = initCurriculum();
        String requestJson=ow.writeValueAsString(curriculumDto);
        log.info(requestJson);

        this.mockMvc.perform(post("/curriculum/createCurriculum")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret)
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is2xxSuccessful());


        MvcResult result = this.mockMvc.perform(get("/curriculum/getCurriculum")
                        .contentType(APPLICATION_JSON)
                        .param("name", curriculumDto.getName()))
                .andExpect(status().is2xxSuccessful()).andReturn();

        log.info(result.getResponse().getContentAsString());
    }

    @Test
    @Transactional
    void shouldGetAllCurriculum() throws Exception {
        CurriculumDto curriculumDto = initCurriculum();

        CurriculumDto curriculumDto2 = new CurriculumDto();
        curriculumDto2.setName("test curriculum 2");
        curriculumDto2.setDescription("test description 2");
        curriculumDto2.setDateStart(LocalDate.parse("2026-05-14"));
        curriculumDto2.setDateEnd(LocalDate.parse("2027-05-14"));

        String requestJson=ow.writeValueAsString(curriculumDto);
        String requestJson2=ow.writeValueAsString(curriculumDto2);

        log.info(requestJson);
        log.info(requestJson2);

        this.mockMvc.perform(post("/curriculum/createCurriculum")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret)
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is2xxSuccessful());
        this.mockMvc.perform(post("/curriculum/createCurriculum")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret)
                        .contentType(APPLICATION_JSON)
                        .content(requestJson2))
                .andExpect(status().is2xxSuccessful());


        MvcResult result = this.mockMvc.perform(get("/curriculum/getAllCurriculums"))
                .andExpect(status().is2xxSuccessful()).andReturn();

        log.info(result.getResponse().getContentAsString());
    }

    @Test
    @Transactional
    void shouldGetAllCurriculumPaged() throws Exception {
        CurriculumDto curriculumDto = initCurriculum();

        CurriculumDto curriculumDto2 = new CurriculumDto();
        curriculumDto2.setName("test curriculum 2");
        curriculumDto2.setDescription("test description 2");
        curriculumDto2.setDateStart(LocalDate.parse("2026-05-14"));
        curriculumDto2.setDateEnd(LocalDate.parse("2027-05-14"));

        String requestJson=ow.writeValueAsString(curriculumDto);
        String requestJson2=ow.writeValueAsString(curriculumDto2);

        log.info(requestJson);
        log.info(requestJson2);

        this.mockMvc.perform(post("/curriculum/createCurriculum")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret)
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is2xxSuccessful());
        this.mockMvc.perform(post("/curriculum/createCurriculum")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret)
                        .contentType(APPLICATION_JSON)
                        .content(requestJson2))
                .andExpect(status().is2xxSuccessful());

        MvcResult result = this.mockMvc.perform(get("/curriculum/getAllCurriculums/paged")
                        .param("page", "1")
                        .param("size", "1"))
                .andExpect(status().is2xxSuccessful()).andReturn();

        log.info(result.getResponse().getContentAsString());
    }

    @Test
    @Transactional
    void shouldUpdateCurriculum() throws Exception {
        CurriculumDto curriculumDto = initCurriculum();
        String requestJson=ow.writeValueAsString(curriculumDto);
        log.info(requestJson);

        this.mockMvc.perform(post("/curriculum/createCurriculum")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret)
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is2xxSuccessful());

        CurriculumDto curriculumDtoMod = new CurriculumDto();
        curriculumDtoMod.setName("test curriculum mod");
        curriculumDtoMod.setDescription("test description mod");
        curriculumDtoMod.setDateStart(null);
        curriculumDtoMod.setDateEnd(null);

        String requestJsonMod=ow.writeValueAsString(curriculumDtoMod);

        MvcResult result = this.mockMvc.perform(
                patch("/curriculum/updateCurriculum/{name}", curriculumDto.getName())
                        .contentType(APPLICATION_JSON)
                        .content(requestJsonMod)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret))
                .andExpect(status().is2xxSuccessful()).andReturn();

        log.info(result.getResponse().getContentAsString());
    }

    @Test
    @Transactional
    void shouldAddAndGetAllSubjectToCurriculumAndDelete() throws Exception {
        CurriculumDto curriculumDto = initCurriculum();
        String requestJson=ow.writeValueAsString(curriculumDto);
        log.info(requestJson);

        this.mockMvc.perform(post("/curriculum/createCurriculum")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret)
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is2xxSuccessful());

        SubjectDto subjectDto = new SubjectDto();
        subjectDto.setName("test subject");
        subjectDto.setDescription("test description");
        subjectDto.setStartDate(LocalDate.parse("2026-05-14"));
        subjectDto.setEndDate(LocalDate.parse("2027-05-14"));
        subjectDto.setRemote(true);
        subjectDto.setOnSite(true);
        subjectDto.setRoomLocation("18-302");
        subjectDto.setCreditsValue(26);

        String requestSubjectJson=ow.writeValueAsString(subjectDto);
        log.info(requestSubjectJson);

        this.mockMvc.perform(
                        post("/subject/createSubject")
                                .contentType(APPLICATION_JSON)
                                .content(requestSubjectJson)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret))
                .andExpect(status().is2xxSuccessful());

        MvcResult resultAdd = this.mockMvc.perform(
                        put("/curriculum/addSubject/{curriculumName}", curriculumDto.getName())
                                .contentType(APPLICATION_JSON)
                                .content(requestSubjectJson)
                                .param("subjectName", subjectDto.getName())
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret))
                .andExpect(status().is2xxSuccessful()).andReturn();
        log.info(resultAdd.getResponse().getContentAsString());

        MvcResult resultGetAll = this.mockMvc.perform(
                        get("/curriculum/getAllSubjects/{curriculumName}", curriculumDto.getName())
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret))
                .andExpect(status().is2xxSuccessful()).andReturn();
        log.info(resultGetAll.getResponse().getContentAsString());

        this.mockMvc.perform(
                        delete("/curriculum/removeSubject/{curriculumName}", curriculumDto.getName())
                                .contentType(APPLICATION_JSON)
                                .param("subjectName", subjectDto.getName())
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret))
                .andExpect(status().is2xxSuccessful());
    }
}
