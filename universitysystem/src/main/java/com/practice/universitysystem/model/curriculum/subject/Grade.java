package com.practice.universitysystem.model.curriculum.subject;

import com.practice.universitysystem.model.users.student.student_subject.StudentSubjectRegistrationId;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "5")
    private double gradeValue;

    @NotNull
    @DecimalMin(value = "0.01")
    @DecimalMax(value = "100")
    private double percentageOfFinalGrade;

    @NotNull
    private StudentSubjectRegistrationId registrationId;

    @NotNull
    private String description;

}
