package com.practice.universitysystem.model.users;

import lombok.Data;
import org.hibernate.annotations.Check;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
@Inheritance(strategy = InheritanceType.JOINED)
@Check(constraints = "mobile_phone IS NOT NULL OR land_phone IS NOT NULL")
@Table(uniqueConstraints = {
        @UniqueConstraint(
                name = "username_gov_id_email_is_unique",
                columnNames =
                {
                        "username", "government_id", "email"
                })
})
public abstract class UniversityUser {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @NotNull
    private String name;

    @NotNull
    private String lastName;

    @NotNull
    @Column(name = "government_id")
    private String governmentId;

    @NotNull
    @Email
    private String email;

    private String mobilePhone;
    private String landPhone;

    @Temporal(TemporalType.DATE)
    private Date birthdate;

    @NotNull
    private String userPassword;

    @NotNull
    private String username;

    @NotNull
    @Temporal(TemporalType.DATE)
    @CreatedDate
    private Date enrollmentDate;

}
