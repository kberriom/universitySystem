package com.practice.universitysystem.model;

import lombok.Data;
import org.hibernate.annotations.Check;

import javax.annotation.Generated;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
@Inheritance(strategy = InheritanceType.JOINED)
@Check(constraints = "mobile_phone IS NOT NULL OR land_phone IS NOT NULL")
public abstract class UniversityUser {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @NotNull
    private String name;
    @NotNull
    private String lastName;
    @NotNull
    private String governmentId;
    @NotNull
    private String email;

    private String mobilePhone;
    private String landPhone;

    @Temporal(TemporalType.DATE)
    private Date birthdate;
}