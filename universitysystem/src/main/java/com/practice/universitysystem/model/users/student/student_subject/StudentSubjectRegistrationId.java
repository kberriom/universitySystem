package com.practice.universitysystem.model.users.student.student_subject;

import lombok.Data;

import jakarta.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class StudentSubjectRegistrationId implements Serializable {

    //FK
    private Long studentUserId;

    //FK
    private Long subjectId;

}
