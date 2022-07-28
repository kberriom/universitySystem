package com.practice.universitysystem.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
@Data
public class StudentSubjectRegistrationId implements Serializable {

    //FK
    private Long studentUserId;

    //FK
    private Long subjectId;

}
