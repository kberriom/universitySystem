package com.practice.universitysystem.model.users.admin;

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
@PrimaryKeyJoinColumn(name = "adminUserId")
public class Admin extends UniversityUser {

    //id is a FK from UniversityUser

    @NotNull
    @Setter(AccessLevel.NONE)
    @Getter
    private String role = "ROLE_ADMIN";

}
