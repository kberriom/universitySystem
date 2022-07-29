package com.practice.universitysystem.model.curriculum;

import com.practice.universitysystem.model.curriculum.subject.Subject;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Data
@Entity
public class Curriculum {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @NotNull
    String name;

    @NotNull
    String description;

    @Temporal(TemporalType.DATE)
    @NotNull
    private Date dateStart;

    @Temporal(TemporalType.DATE)
    @NotNull
    private Date dateEnd;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "curriculum_subjects",
            joinColumns = @JoinColumn(name = "curriculum_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    Set<Subject> subjects;

}
