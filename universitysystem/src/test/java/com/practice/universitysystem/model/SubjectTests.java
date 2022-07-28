package com.practice.universitysystem.model;

import com.practice.universitysystem.repository.SubjectRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class SubjectTests {

    @Autowired
    SubjectRepository subjectRepository;

    @Test
    @Transactional
    void createAndDeleteSubject() throws ParseException {
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

        subjectRepository.save(subject);

        assertEquals(1, subjectRepository.count());

        subjectRepository.delete(subject);

        assertEquals(0, subjectRepository.count());

    }

}
