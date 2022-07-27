package com.practice.universitysystem.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Data
public class CourseSubject {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int courseSubjectId;

    private String name;

    String description;

    @Temporal(TemporalType.DATE)
    private Date dateStart;

    @Temporal(TemporalType.DATE)
    private Date dateEnd;

    private boolean isRemote;

    private boolean isOnSite;

    private boolean creditsValue;

    //Student registration relation
    @ManyToMany(mappedBy = "enrolledCourses")
    private Set<Student> students;

    //Teacher assignation relation
    @ManyToMany(mappedBy = "assignedCourses")
    private Set<Teacher> teachers;

    //Subjects in course
    @ManyToMany(mappedBy = "courseSubjects")
    private Set<Course> courses;
}
