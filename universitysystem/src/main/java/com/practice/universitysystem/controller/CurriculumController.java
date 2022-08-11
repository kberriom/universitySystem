package com.practice.universitysystem.controller;

import com.practice.universitysystem.dto.CurriculumDto;
import com.practice.universitysystem.model.curriculum.Curriculum;
import com.practice.universitysystem.model.curriculum.subject.Subject;
import com.practice.universitysystem.service.curriculum.CurriculumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/curriculum")
public class CurriculumController {

    @Autowired
    CurriculumService curriculumService;

    @PostMapping("/createCurriculum")
    @ResponseStatus(HttpStatus.CREATED)
    @Secured("ROLE_ADMIN")
    public Curriculum createCurriculum(@RequestBody CurriculumDto curriculumDto) {
        return curriculumService.createCurriculum(curriculumDto);
    }

    @DeleteMapping("/deleteCurriculum")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured("ROLE_ADMIN")
    public void deleteCurriculum(@RequestParam String name) {
        curriculumService.deleteCurriculum(name);
    }

    @GetMapping("/getCurriculum")
    @ResponseStatus(HttpStatus.OK)
    public Curriculum getCurriculum(@RequestParam String name) {
        return curriculumService.getCurriculum(name);
    }

    @PatchMapping("/updateCurriculum/{name}")
    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_ADMIN")
    public Curriculum updateCurriculum(@PathVariable String name, @RequestBody CurriculumDto curriculumDto) {
        return curriculumService.updateCurriculum(name, curriculumDto);
    }

    @PutMapping("/addSubject/{curriculumName}")
    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_ADMIN")
    public List<Object> addSubject(@PathVariable String curriculumName, @RequestParam String subjectName) {
        return curriculumService.addSubject(curriculumName, subjectName);
    }

    @DeleteMapping("/removeSubject/{curriculumName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured("ROLE_ADMIN")
    public void removeSubject(@PathVariable String curriculumName, @RequestParam String subjectName) {
        curriculumService.removeSubject(curriculumName, subjectName);
    }

    @GetMapping("/getAllSubjects/{curriculumName}")
    @ResponseStatus(HttpStatus.OK)
    public Set<Subject> getAllSubjects(@PathVariable String curriculumName) {
        return curriculumService.getAllSubjectsInCurriculum(curriculumName);
    }

}
