package com.practice.universitysystem.model.users.student.student_subject;

import com.practice.universitysystem.model.curriculum.subject.Grade;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class StudentSubjectRegistration {
    @EmbeddedId
    private StudentSubjectRegistrationId id;

    @NotNull
    @Temporal(TemporalType.DATE)
    @CreatedDate
    private Date registrationDate;

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

}
