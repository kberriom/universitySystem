package com.practice.universitysystem.model.users.teacher.teacher_asignation;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
@Data
@NoArgsConstructor
public class TeacherAssignation {
    @EmbeddedId
    private TeacherAssignationId id;

    @NotNull
    private String roleInClass;

    public TeacherAssignation(Long teacherUserId, Long subjectId) {
        TeacherAssignationId assignationId = new TeacherAssignationId();
        assignationId.setTeacherUserId(teacherUserId);
        assignationId.setSubjectId(subjectId);
        this.id = assignationId;
    }

}
