package com.practice.universitysystem.model.curriculum.subject;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.practice.universitysystem.model.curriculum.Curriculum;
import lombok.Data;
import org.hibernate.annotations.Check;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
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

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @NotNull
    private LocalDate startDate;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @NotNull
    private LocalDate endDate;

    private boolean isRemote;
    private boolean isOnSite;
    private String roomLocation;

    @NotNull
    private Integer creditsValue;

    @ManyToMany(cascade = CascadeType.ALL)
    Set<Curriculum> curriculumsContainingSubject;

}
