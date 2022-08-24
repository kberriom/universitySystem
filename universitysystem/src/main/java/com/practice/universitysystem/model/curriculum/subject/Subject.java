package com.practice.universitysystem.model.curriculum.subject;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.Check;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Data
@Check(constraints =
        "(remote IS NOT NULL OR on_site IS NOT NULL) AND (on_site IS NOT NULL AND room_location IS NOT NULL) AND (start_date <= end_date)")
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

    @Column(name = "remote")
    private Boolean remote;
    @Column(name = "on_site")
    private Boolean onSite;
    private String roomLocation;

    @NotNull
    private Integer creditsValue;

    /*
    Lombok generates non-standard named setters and getters for booleans
    and that causes conflicts with libs (spring parser, mapstruct)
    */

    public boolean isRemote() {
        return remote;
    }

    public void setRemote(boolean remote) {
        this.remote = remote;
    }

    public boolean isOnSite() {
        return onSite;
    }

    public void setOnSite(boolean onSite) {
        this.onSite = onSite;
    }
}
