package com.practice.universitysystem.model.curriculum;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.practice.universitysystem.model.curriculum.subject.Subject;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

@Data
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(
                name = "curriculum_name_is_unique",
                columnNames = {"name"})
})
public class Curriculum {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @NotNull
    String name;

    @NotNull
    String description;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @NotNull
    private LocalDate dateStart;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @NotNull
    private LocalDate dateEnd;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "curriculum_subjects",
            joinColumns = @JoinColumn(name = "curriculum_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    Set<Subject> subjects;

}
