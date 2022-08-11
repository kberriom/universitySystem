package com.practice.universitysystem.controller;

import com.practice.universitysystem.dto.SubjectDto;
import com.practice.universitysystem.model.curriculum.subject.Subject;
import com.practice.universitysystem.service.subject.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subject")
public class SubjectController {

    @Autowired
    SubjectService subjectService;

    @PostMapping("/createSubject")
    @ResponseStatus(HttpStatus.CREATED)
    @Secured("ROLE_ADMIN")
    public Subject createSubject(@RequestBody SubjectDto subjectDto) {
        return subjectService.createSubject(subjectDto);
    }

    @GetMapping("/getSubject")
    @ResponseStatus(HttpStatus.OK)
    public Subject getSubject(@RequestParam String name) {
        return subjectService.getSubject(name);
    }

    @PatchMapping("/updateSubject/{name}")
    @ResponseStatus(HttpStatus.OK)
    @Secured("ROLE_ADMIN")
    public Subject updateSubject(@PathVariable String name, @RequestBody SubjectDto subjectDto) {
        return subjectService.updateSubject(name, subjectDto);
    }

    @DeleteMapping("/deleteSubject")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured("ROLE_ADMIN")
    public void deleteSubject(@RequestParam String name) {
        subjectService.deleteSubject(name);
    }
}
