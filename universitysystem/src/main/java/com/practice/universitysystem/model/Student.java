package com.practice.universitysystem.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.util.Date;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "studentUserId")
public class Student extends UniversityUser {

    //id is a FK from UniversityUser

    /**
     * Date that a student was enrolled on, by default is
     * assigned the current date
     */
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date enrollmentDate = new Date();

}
