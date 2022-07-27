package com.practice.universitysystem.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "teacherId")
public class Teacher extends User{

    private String teacherId;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "teacher_assigned_subject",
            joinColumns = @JoinColumn(name = "teacher_id", referencedColumnName = "teacherId"),
            inverseJoinColumns = @JoinColumn(name = "course_subject_id", referencedColumnName = "courseSubjectId"))
    private Set<Course> assignedCourses;


}
