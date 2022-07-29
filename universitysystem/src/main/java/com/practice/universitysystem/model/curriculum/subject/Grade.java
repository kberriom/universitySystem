package com.practice.universitysystem.model.curriculum.subject;

import com.practice.universitysystem.model.users.student.student_subject.StudentSubjectRegistration;
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

    @ManyToOne
    @JoinColumn(name = "student_user_id", insertable = false, updatable = false)
    @JoinColumn(name = "subject_id", insertable = false, updatable = false)
    private StudentSubjectRegistration registration;

    @NotNull
    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Grade grade = (Grade) o;

        if (id != grade.id) return false;
        if (Double.compare(grade.gradeValue, gradeValue) != 0) return false;
        if (Double.compare(grade.percentageOfFinalGrade, percentageOfFinalGrade) != 0) return false;
        return description.equals(grade.description);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (id ^ (id >>> 32));
        temp = Double.doubleToLongBits(gradeValue);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(percentageOfFinalGrade);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + description.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "id=" + id +
                ", gradeValue=" + gradeValue +
                ", percentageOfFinalGrade=" + percentageOfFinalGrade +
                ", description='" + description + '\'' +
                '}';
    }
}
