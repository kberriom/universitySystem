package com.practice.universitysystem.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
public class StudentSubjectRegistration {
    @EmbeddedId
    private StudentSubjectRegistrationId id;

    @NotNull
    @Setter(AccessLevel.NONE)
    private Date registrationDate = new Date();

    public StudentSubjectRegistration(Long studentUserId, Long subjectId){
        StudentSubjectRegistrationId registrationId = new StudentSubjectRegistrationId();

        registrationId.setStudentUserId(studentUserId);
        registrationId.setSubjectId(subjectId);
        this.id = registrationId;
    }

    public StudentSubjectRegistration(){
    }

}
