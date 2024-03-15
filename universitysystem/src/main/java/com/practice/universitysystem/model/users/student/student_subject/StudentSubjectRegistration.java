package com.practice.universitysystem.model.users.student.student_subject;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.practice.universitysystem.model.curriculum.subject.Grade;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class StudentSubjectRegistration {
    @EmbeddedId
    private StudentSubjectRegistrationId id;

    @NotNull
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @CreatedDate
    private LocalDate registrationDate;

    @DecimalMin(value = "0")
    @DecimalMax(value = "5")
    private Double finalGrade;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Grade> subjectGrades;

    public StudentSubjectRegistration(Long studentUserId, Long subjectId) {
        StudentSubjectRegistrationId registrationId = new StudentSubjectRegistrationId();

        registrationId.setStudentUserId(studentUserId);
        registrationId.setSubjectId(subjectId);
        this.id = registrationId;
    }

}
