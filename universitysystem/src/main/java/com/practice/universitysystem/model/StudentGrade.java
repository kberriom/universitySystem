package com.practice.universitysystem.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table
public class StudentGrade {

    @Id
    private long gradeId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "student_id")
    private Student student;

}
