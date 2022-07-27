package com.practice.universitysystem.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "studentId")
public class Student extends User{

    private String studentId;

    private int currentCredits;

    @OneToOne(mappedBy = "student")
    private StudentGrade studentGrade;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "student_enrolled_subject",
            joinColumns = @JoinColumn(name = "student_id", referencedColumnName = "studentId"),
            inverseJoinColumns = @JoinColumn(name = "course_subject_id", referencedColumnName = "courseSubjectId"))
    private Set<Course> enrolledCourses;
}
