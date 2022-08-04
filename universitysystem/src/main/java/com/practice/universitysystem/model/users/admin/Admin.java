package com.practice.universitysystem.model.users.admin;

import com.practice.universitysystem.model.users.UniversityUser;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.constraints.NotNull;

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
