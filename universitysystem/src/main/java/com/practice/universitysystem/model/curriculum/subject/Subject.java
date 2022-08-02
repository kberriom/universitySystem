package com.practice.universitysystem.model.curriculum.subject;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.practice.universitysystem.model.curriculum.Curriculum;
import lombok.Data;
import org.hibernate.annotations.Check;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Entity
@Data
@Check(constraints =
        "(is_remote IS NOT NULL OR is_on_site IS NOT NULL) AND (is_on_site IS NOT NULL AND room_location IS NOT NULL)")
@Table(uniqueConstraints = {
        @UniqueConstraint(
                name = "subject_name_is_unique",
                columnNames = {"name"})
})
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(style = "dd-MM-yyyy")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="dd-MM-yyyy")
    @NotNull
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(style = "dd-MM-yyyy")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="dd-MM-yyyy")
    @NotNull
    private Date endDate;

    private boolean isRemote;
    private boolean isOnSite;
    private String roomLocation;

    @NotNull
    private int creditsValue;

    @ManyToMany(cascade = CascadeType.ALL)
    Set<Curriculum> curriculumsContainingSubject;

}
