package com.practice.universitysystem.model.curriculum.subject;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.practice.universitysystem.model.users.student.student_subject.StudentSubjectRegistrationId;
import lombok.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

@Data
@Entity
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "5")
    private Double gradeValue;

    @NotNull
    @DecimalMin(value = "0.01")
    @DecimalMax(value = "100")
    private Double percentageOfFinalGrade;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private StudentSubjectRegistrationId registrationId;

    @NotNull
    private String description;

}
