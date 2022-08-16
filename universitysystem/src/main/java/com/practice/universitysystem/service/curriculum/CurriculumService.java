package com.practice.universitysystem.service.curriculum;

import com.practice.universitysystem.dto.CurriculumDto;
import com.practice.universitysystem.model.curriculum.Curriculum;
import com.practice.universitysystem.model.curriculum.subject.Subject;
import com.practice.universitysystem.repository.curriculum.CurriculumRepository;
import com.practice.universitysystem.repository.curriculum.subject.SubjectRepository;
import com.practice.universitysystem.service.ServiceUtils;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CurriculumService {

    CurriculumRepository curriculumRepository;
    SubjectRepository subjectRepository;

    ServiceUtils<Curriculum, Long, CurriculumRepository> serviceUtilsCurriculum;

    @Autowired
    public CurriculumService(CurriculumRepository curriculumRepository, SubjectRepository subjectRepository) {
        this.curriculumRepository = curriculumRepository;
        this.subjectRepository = subjectRepository;
        serviceUtilsCurriculum = new ServiceUtils<>(curriculumRepository);
    }

    private static final CurriculumMapper mapper = Mappers.getMapper(CurriculumMapper.class);

    public Curriculum createCurriculum(CurriculumDto curriculumDto) {
        Curriculum curriculum = mapper.dtoToCurriculum(curriculumDto);
        serviceUtilsCurriculum.validate(curriculum);
        return curriculumRepository.save(curriculum);
    }

    public void deleteCurriculum(String name) {
        Curriculum curriculum = getCurriculum(name);
        curriculumRepository.delete(curriculum);
    }

    public Curriculum getCurriculum(String name) {
        return curriculumRepository.findByName(name).orElseThrow(()->
                new NoSuchElementException("Unable to find Curriculum with name: " + name));
    }

    public List<Curriculum> getAllCurriculum() {
        return curriculumRepository.findAll();
    }

    public List<Object> getPaginatedCurriculum(int page, int pageSize) {
        return serviceUtilsCurriculum.getPaginatedList(page, pageSize);
    }

    public Curriculum updateCurriculum(String name, CurriculumDto curriculumDto) {
        Curriculum curriculum =  mapper.update(getCurriculum(name), curriculumDto);
        serviceUtilsCurriculum.validate(curriculum);
        return curriculumRepository.save(curriculum);
    }

    public List<Object> addSubject(String curriculumName, String subjectName) {
        Curriculum curriculum = getCurriculum(curriculumName);
        if (curriculum.getSubjects() == null) {
            curriculum.setSubjects(new HashSet<>());
        }

        Subject subjectToAdd = subjectRepository.findByName(subjectName).orElseThrow(()->
                new NoSuchElementException("Unable to add nonexistent Subject with subjectName: "+ subjectName));

        curriculum.getSubjects().add(subjectToAdd);
        Curriculum savedCurriculum = curriculumRepository.save(curriculum);

        List<Object> responseList = new ArrayList<>(curriculum.getSubjects().size()+1);
        responseList.add(savedCurriculum);
        responseList.addAll(savedCurriculum.getSubjects());

        return responseList;
    }

    public void removeSubject(String curriculumName, String subjectName) {
        Curriculum curriculum = getCurriculum(curriculumName);
        Subject subjectToDelete = subjectRepository.findByName(subjectName).
                orElseThrow(()-> new NoSuchElementException("Unable to delete nonexistent Subject with subjectName: "+ subjectName));
        curriculum.getSubjects().remove(subjectToDelete);
        curriculumRepository.save(curriculum);
    }

    public Set<Subject> getAllSubjectsInCurriculum(String curriculumName) {
        Curriculum curriculum = getCurriculum(curriculumName);
        return curriculum.getSubjects();
    }

}
