package com.practice.universitysystem.model.users.student;

import com.practice.universitysystem.model.users.UniversityUser;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "studentUserId")
public class Student extends UniversityUser {

    //id is a FK from UniversityUser

    @NotNull
    @Setter(AccessLevel.NONE)
    @Getter
    private String role = "ROLE_STUDENT";

}
