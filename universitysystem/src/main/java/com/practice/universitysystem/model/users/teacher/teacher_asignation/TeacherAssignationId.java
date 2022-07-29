package com.practice.universitysystem.model.users.teacher.teacher_asignation;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class TeacherAssignationId implements Serializable {

    //FK
    private Long subjectId;

    //FK
    private Long teacherUserId;

}
