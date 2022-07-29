package com.practice.universitysystem.model.student_subject;

import com.practice.universitysystem.model.Grade;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Entity
@Data
public class StudentSubjectRegistration {
    @EmbeddedId
    private StudentSubjectRegistrationId id;

    @NotNull
    @Setter(AccessLevel.NONE)
    private final Date registrationDate = new Date();

    @DecimalMin(value = "0")
    @DecimalMax(value = "5")
    private double finalGrade;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Grade> subjectGrades;

    public StudentSubjectRegistration(Long studentUserId, Long subjectId) {
        StudentSubjectRegistrationId registrationId = new StudentSubjectRegistrationId();

        registrationId.setStudentUserId(studentUserId);
        registrationId.setSubjectId(subjectId);
        this.id = registrationId;
    }

    public StudentSubjectRegistration() {
    }

}
