package com.practice.universitysystem.model;

import lombok.Data;
import org.hibernate.annotations.Check;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
@Check(constraints =
        "(is_remote IS NOT NULL OR is_on_site IS NOT NULL) AND (is_on_site IS NOT NULL AND room_location IS NOT NULL)")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    private String name;
    @NotNull
    private String description;
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Temporal(TemporalType.DATE)
    private Date endDate;
    private boolean isRemote;
    private boolean isOnSite;
    private String roomLocation;
    @NotNull
    private int creditsValue;



}
