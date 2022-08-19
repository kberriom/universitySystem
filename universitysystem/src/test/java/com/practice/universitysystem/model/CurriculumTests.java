package com.practice.universitysystem.model;

import com.practice.universitysystem.model.curriculum.Curriculum;
import com.practice.universitysystem.model.curriculum.subject.Subject;
import com.practice.universitysystem.repository.curriculum.CurriculumRepository;
import com.practice.universitysystem.repository.curriculum.subject.SubjectRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static com.practice.universitysystem.model.SubjectTests.getSubject;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CurriculumTests {

    @Autowired
    CurriculumRepository curriculumRepository;

    @Autowired
    SubjectRepository subjectRepository;

    private static Validator validator;

    @BeforeAll
    public static void validationSetup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @Transactional
    void createAndDeleteCurriculum() throws ParseException {
        assertEquals(0, curriculumRepository.count());
        assertEquals(0, subjectRepository.count());

        Curriculum curriculum = new Curriculum();
        curriculum.setName("CURRICULUM_NAME");
        curriculum.setDescription("CURRICULUM_DESCRIPTION");
        curriculum.setDateStart(LocalDate.parse("2000-05-14"));
        curriculum.setDateEnd(LocalDate.parse("2001-05-14"));

        Subject subject1 = getSubject("SUBJECT1");
        Set<ConstraintViolation<Subject>> constraintViolationsSubject1 = validator.validate(subject1);
        if (!constraintViolationsSubject1.isEmpty()) {
            throw new ConstraintViolationException(constraintViolationsSubject1);
        }
        subjectRepository.save(subject1);

        Subject subject2 = getSubject("SUBJECT2");
        Set<ConstraintViolation<Subject>> constraintViolationsSubject2 = validator.validate(subject2);
        if (!constraintViolationsSubject2.isEmpty()) {
            throw new ConstraintViolationException(constraintViolationsSubject2);
        }
        subjectRepository.save(subject2);

        assertEquals(2, subjectRepository.count());

        curriculum.setSubjects(new HashSet<>());

        curriculum.getSubjects().add(subject1);
        curriculum.getSubjects().add(subject2);

        Set<ConstraintViolation<Curriculum>> constraintViolationsCurriculum = validator.validate(curriculum);
        if (!constraintViolationsCurriculum.isEmpty()) {
            throw new ConstraintViolationException(constraintViolationsCurriculum);
        }

        curriculumRepository.save(curriculum);

        assertEquals(1, curriculumRepository.count());
        assertEquals(2, curriculumRepository.getReferenceById(curriculum.getId()).getSubjects().size());

    }

}
