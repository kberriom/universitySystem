package com.practice.universitysystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.practice.universitysystem.dto.CurriculumDto;
import com.practice.universitysystem.dto.SubjectDto;
import com.practice.universitysystem.model.curriculum.Curriculum;
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

    private int generatedCurriculumCount = 0;

    CurriculumDto initCurriculum() {
        generatedCurriculumCount++;
        CurriculumDto curriculumDto = new CurriculumDto();
        curriculumDto.setName("test curriculum " + generatedCurriculumCount);
        curriculumDto.setDescription("test description " + generatedCurriculumCount);
        curriculumDto.setDateStart(LocalDate.parse("2026-05-"+Math.min(14+generatedCurriculumCount, 30)));
        curriculumDto.setDateEnd(LocalDate.parse("2027-06-"+Math.min(14+generatedCurriculumCount, 30)));
        return curriculumDto;
    }

    Curriculum createCurriculum(CurriculumDto curriculumDto) throws Exception {
        String requestJson=ow.writeValueAsString(curriculumDto);
        log.info(requestJson);
        MvcResult result = this.mockMvc.perform(post("/curriculum/createCurriculum")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret)
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is2xxSuccessful()).andReturn();
        log.info(result.getResponse().getContentAsString());
        return mapper.readValue(result.getResponse().getContentAsString(), Curriculum.class);
    }

    @Test
    @Transactional
    void shouldCreateCurriculum() throws Exception {
        CurriculumDto curriculumDto = initCurriculum();
        Curriculum curriculum = createCurriculum(curriculumDto);
    }

    @Test
    @Transactional
    void shouldDeleteCurriculum() throws Exception {
        CurriculumDto curriculumDto = initCurriculum();
        Curriculum curriculum = createCurriculum(curriculumDto);

        this.mockMvc.perform(delete("/curriculum/deleteCurriculum")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminSecret)
                        .contentType(APPLICATION_JSON)
                .param("name", curriculum.getName()))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @Transactional
    void shouldGetCurriculum() throws Exception {
        CurriculumDto curriculumDto = initCurriculum();
        Curriculum curriculum = createCurriculum(curriculumDto);

        MvcResult result = this.mockMvc.perform(get("/curriculum/getCurriculum")
                        .contentType(APPLICATION_JSON)
                        .param("name", curriculum.getName()))
                .andExpect(status().is2xxSuccessful()).andReturn();

        log.info(result.getResponse().getContentAsString());
    }

    @Test
    @Transactional
    void shouldGetAllCurriculum() throws Exception {
        CurriculumDto curriculumDto = initCurriculum();
        Curriculum curriculum = createCurriculum(curriculumDto);

        CurriculumDto curriculumDto2 = initCurriculum();
        Curriculum curriculum2 = createCurriculum(curriculumDto2);

        MvcResult result = this.mockMvc.perform(get("/curriculum/getAllCurriculums"))
                .andExpect(status().is2xxSuccessful()).andReturn();

        log.info(result.getResponse().getContentAsString());
    }

    @Test
    @Transactional
    void shouldGetAllCurriculumPaged() throws Exception {
        CurriculumDto curriculumDto = initCurriculum();
        Curriculum curriculum = createCurriculum(curriculumDto);

        CurriculumDto curriculumDto2 = initCurriculum();
        Curriculum curriculum2 = createCurriculum(curriculumDto2);

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
        Curriculum curriculum = createCurriculum(curriculumDto);

        CurriculumDto curriculumDtoMod = new CurriculumDto();
        curriculumDtoMod.setName("test curriculum mod");
        curriculumDtoMod.setDescription("test description mod");
        curriculumDtoMod.setDateStart(null);
        curriculumDtoMod.setDateEnd(null);

        String requestJsonMod=ow.writeValueAsString(curriculumDtoMod);

        MvcResult result = this.mockMvc.perform(
                patch("/curriculum/updateCurriculum/{name}", curriculum.getName())
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
        Curriculum curriculum = createCurriculum(curriculumDto);

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
