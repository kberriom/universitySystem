package com.practice.universitysystem.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Data
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int courseId;

    String name;

    String description;

    @Temporal(TemporalType.DATE)
    private Date dateStart;

    @Temporal(TemporalType.DATE)
    private Date dateEnd;

    

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "subject_in_course",
            joinColumns = @JoinColumn(name = "course_id", referencedColumnName = "courseId"),
            inverseJoinColumns = @JoinColumn(name = "course_subject_id", referencedColumnName = "courseSubjectId"))
    Set<CourseSubject> courseSubjects;
}
