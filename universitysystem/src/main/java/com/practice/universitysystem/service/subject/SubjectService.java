package com.practice.universitysystem.service.subject;

import com.practice.universitysystem.dto.SubjectDto;
import com.practice.universitysystem.model.curriculum.subject.Subject;
import com.practice.universitysystem.model.users.student.Student;
import com.practice.universitysystem.repository.curriculum.subject.SubjectRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.*;
import java.util.Set;

@Service
public class SubjectService {

    @Autowired
    SubjectRepository subjectRepository;

    private static final SubjectMapper mapper = Mappers.getMapper(SubjectMapper.class);

    public Subject createSubject(SubjectDto subjectDto) {
        Subject subject = mapper.dtoToSubject(subjectDto);
        validateSubject(subject);
        return subjectRepository.save(subject);
    }

    private void validateSubject(Subject subject) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Subject>> constraintViolations = validator.validate(subject);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }

    public Subject getSubject(String name) {
        return subjectRepository.findByName(name).orElseThrow();
    }

    public Subject updateSubject(String name, SubjectDto subjectDto) {
        return subjectRepository.save(mapper.update(getSubject(name), subjectDto));
    }

    public void deleteSubject(String name) {
        subjectRepository.delete(getSubject(name));
    }

    public void getAllStudents(String subjectName) {
    }

    public void addStudent(Student student) {
        //todo
    }

    public void removeStudent(Student student) {
        //todo
    }

    public void getAllTeachers() {
        //todo
    }

    public void addTeacher() {
        //todo
    }

    public void removeTeacher() {
        //todo
    }

    public void modifyTeacherRoleInSubject() {
        //todo
    }


}
