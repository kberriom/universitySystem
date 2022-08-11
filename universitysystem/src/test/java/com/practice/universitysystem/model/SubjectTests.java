package com.practice.universitysystem.model;

import com.practice.universitysystem.model.curriculum.subject.Subject;
import com.practice.universitysystem.repository.curriculum.subject.SubjectRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import javax.validation.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class SubjectTests {

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
    void createAndDeleteSubjectTest() throws ParseException {
        assertEquals(0, subjectRepository.count());

        Subject subject = getSubject("SUBJECT1");
        Set<ConstraintViolation<Subject>> constraintViolationsSubject = validator.validate(subject);
        if (!constraintViolationsSubject.isEmpty()) {
            throw new ConstraintViolationException(constraintViolationsSubject);
        }

        subjectRepository.save(subject);

        assertEquals(1, subjectRepository.count());

        subjectRepository.delete(subject);

        assertEquals(0, subjectRepository.count());

    }

    protected static Subject getSubject(String name) throws ParseException {
        Subject subject = new Subject();
        subject.setName(name);
        subject.setDescription("CLASS_DESCRIPTION");
        subject.setRemote(true);
        subject.setOnSite(true);
        subject.setRoomLocation("38-404");
        subject.setStartDate(LocalDate.parse("2010-01-01"));
        subject.setEndDate(LocalDate.parse("2010-05-30"));
        subject.setCreditsValue(8);
        return subject;
    }

}
