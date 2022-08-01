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

        Subject subject = getSubject();
        Set<ConstraintViolation<Subject>> constraintViolationsSubject = validator.validate(subject);
        if (!constraintViolationsSubject.isEmpty()) {
            throw new ConstraintViolationException(constraintViolationsSubject);
        }

        subjectRepository.save(subject);

        assertEquals(1, subjectRepository.count());

        subjectRepository.delete(subject);

        assertEquals(0, subjectRepository.count());

    }

    protected static Subject getSubject() throws ParseException {
        Subject subject = new Subject();
        subject.setName("SUBJECT_CLASS_NAME");
        subject.setDescription("CLASS_DESCRIPTION");
        subject.setRemote(true);
        subject.setOnSite(true);
        subject.setRoomLocation("38-404");
        SimpleDateFormat dateFormatStart = new SimpleDateFormat("dd-MM-yyyy");
        subject.setStartDate(dateFormatStart.parse("1-01-2010"));
        SimpleDateFormat dateFormatEnd = new SimpleDateFormat("dd-MM-yyyy");
        subject.setEndDate(dateFormatEnd.parse("30-05-2010"));
        subject.setCreditsValue(8);
        return subject;
    }

}
