package com.practice.universitysystem.model.users;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.Check;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Data
@Inheritance(strategy = InheritanceType.JOINED)
@Check(constraints = "mobile_phone IS NOT NULL OR land_phone IS NOT NULL")
@Table(uniqueConstraints = {
        @UniqueConstraint(
                name = "username_is_unique",
                columnNames = {"username"}
        ),
        @UniqueConstraint(
                name = "government_id_is_unique",
                columnNames = {"government_id"}
        ),
        @UniqueConstraint(
                name = "email_is_unique",
                columnNames = {"email"}
        ),
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

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @NotNull
    private LocalDate birthdate;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String userPassword;

    @NotNull
    private String username;

    @NotNull
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @CreatedDate
    private LocalDate enrollmentDate;

}
