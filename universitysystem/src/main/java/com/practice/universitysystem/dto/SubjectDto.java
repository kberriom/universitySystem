package com.practice.universitysystem.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SubjectDto {

    private String name;

    private String description;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private LocalDate endDate;

    private Boolean remote;

    private Boolean onSite;

    private String roomLocation;

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
