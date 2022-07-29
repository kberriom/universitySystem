package com.practice.universitysystem.model.users.teacher;

import com.practice.universitysystem.model.users.UniversityUser;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.constraints.NotNull;


@Entity
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "teacherUserId")
public class Teacher extends UniversityUser {

    //id is a FK from UniversityUser

    @NotNull
    private String department;

}
