package com.practice.universitysystem.model.users.student;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.practice.universitysystem.model.users.UniversityUser;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.validation.constraints.NotNull;


@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "studentUserId")
public class Student extends UniversityUser {

    //id is a FK from UniversityUser

    @NotNull
    @Setter(AccessLevel.NONE)
    @Getter
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String role = "ROLE_STUDENT";

}
