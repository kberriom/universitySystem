package com.practice.universitysystem.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@PrimaryKeyJoinColumn(name = "userId")
public class Student extends UniversityUser {

    @Temporal(TemporalType.DATE)
    private Date enrollmentDate;


}
