package com.practice.universitysystem.model.users.teacher;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.practice.universitysystem.model.users.UniversityUser;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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

    @NotNull
    @Setter(AccessLevel.NONE)
    @Getter
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String role = "ROLE_TEACHER";

}
