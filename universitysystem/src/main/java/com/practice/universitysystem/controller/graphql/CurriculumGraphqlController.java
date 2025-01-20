package com.practice.universitysystem.controller.graphql;

import com.practice.universitysystem.dto.CurriculumDto;
import com.practice.universitysystem.model.curriculum.Curriculum;
import com.practice.universitysystem.model.curriculum.subject.Subject;
import com.practice.universitysystem.service.curriculum.CurriculumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Set;

@Controller
public class CurriculumGraphqlController {
    @Autowired
    CurriculumService curriculumService;

    @QueryMapping
    public Curriculum curriculum(@Argument String name) {
        return curriculumService.getCurriculum(name);
    }

    @QueryMapping
    public List<Curriculum> curriculums() {
        return curriculumService.getAllCurriculum();
    }

    @QueryMapping
    public List<Object> curriculumsPaged(@Argument int page, @Argument int size) {
        return curriculumService.getPaginatedCurriculum(page, size);
    }

    @QueryMapping
    public Set<Subject> subjectsInCurriculum(@Argument String curriculumName) {
        return curriculumService.getAllSubjectsInCurriculum(curriculumName);
    }

    @MutationMapping
    @Secured("ROLE_ADMIN")
    public Curriculum createCurriculum(@Argument CurriculumDto curriculumDto) {
        return curriculumService.createCurriculum(curriculumDto);
    }

    @MutationMapping
    @Secured("ROLE_ADMIN")
    public void deleteCurriculum(@Argument String name) {
        curriculumService.deleteCurriculum(name);
    }

    @MutationMapping
    @Secured("ROLE_ADMIN")
    public Curriculum updateCurriculum(@Argument String name, @Argument CurriculumDto curriculumDto) {
        return curriculumService.updateCurriculum(name, curriculumDto);
    }

    @MutationMapping
    @Secured("ROLE_ADMIN")
    public List<Object> addSubjectToCurriculum(@Argument String curriculumName, @Argument String subjectName) {
        return curriculumService.addSubject(curriculumName, subjectName);
    }

    @MutationMapping
    @Secured("ROLE_ADMIN")
    public void removeSubjectFromCurriculum(@Argument String curriculumName, @Argument String subjectName) {
        curriculumService.removeSubject(curriculumName, subjectName);
    }
}
