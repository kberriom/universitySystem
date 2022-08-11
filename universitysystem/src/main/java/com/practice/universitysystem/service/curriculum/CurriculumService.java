package com.practice.universitysystem.service.curriculum;

import com.practice.universitysystem.dto.CurriculumDto;
import com.practice.universitysystem.model.curriculum.Curriculum;
import com.practice.universitysystem.model.curriculum.subject.Subject;
import com.practice.universitysystem.repository.curriculum.CurriculumRepository;
import com.practice.universitysystem.repository.curriculum.subject.SubjectRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CurriculumService {

    @Autowired
    CurriculumRepository curriculumRepository;
    @Autowired
    SubjectRepository subjectRepository;

    private static final CurriculumMapper mapper = Mappers.getMapper(CurriculumMapper.class);

    public Curriculum createCurriculum(CurriculumDto curriculumDto) {
        Curriculum curriculum = mapper.dtoToCurriculum(curriculumDto);
        validateCurriculum(curriculum);
        return curriculumRepository.save(curriculum);
    }

    private void validateCurriculum(Curriculum curriculum) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Curriculum>> constraintViolations = validator.validate(curriculum);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }

    public void deleteCurriculum(String name) {
        Curriculum curriculum = getCurriculum(name);
        curriculumRepository.delete(curriculum);
    }

    public Curriculum getCurriculum(String name) {
        return curriculumRepository.findByName(name).orElseThrow();
    }

    public Curriculum updateCurriculum(String name, CurriculumDto curriculumDto) {
        Curriculum curriculum =  mapper.update(getCurriculum(name), curriculumDto);
        return curriculumRepository.save(curriculum);
    }

    public List<Object> addSubject(String curriculumName, String subjectName) {
        Curriculum curriculum = getCurriculum(curriculumName);
        if (curriculum.getSubjects() == null) {
            curriculum.setSubjects(new HashSet<>());
        }

        Subject subjectToAdd = subjectRepository.findByName(subjectName).orElseThrow();

        curriculum.getSubjects().add(subjectToAdd);
        Curriculum savedCurriculum = curriculumRepository.save(curriculum);

        List<Object> responseList = new ArrayList<>(curriculum.getSubjects().size()+1);
        responseList.add(savedCurriculum);
        responseList.addAll(savedCurriculum.getSubjects());

        return responseList;
    }

    public void removeSubject(String curriculumName, String subjectName) {
        Curriculum curriculum = getCurriculum(curriculumName);
        Subject subjectToDelete = subjectRepository.findByName(subjectName).orElseThrow();
        curriculum.getSubjects().remove(subjectToDelete);
        curriculumRepository.save(curriculum);
    }

    public Set<Subject> getAllSubjectsInCurriculum(String curriculumName) {
        Curriculum curriculum = getCurriculum(curriculumName);
        return curriculum.getSubjects();
    }

}
