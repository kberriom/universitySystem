package com.practice.universitysystem.repository.users.teacher.teacher_assignation;

import com.practice.universitysystem.model.users.teacher.teacher_asignation.TeacherAssignation;
import com.practice.universitysystem.model.users.teacher.teacher_asignation.TeacherAssignationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeacherAssignationRepository extends JpaRepository<TeacherAssignation, TeacherAssignationId> {

    @Query("select t from TeacherAssignation t where t.id.subjectId = ?1")
    List<TeacherAssignation> findAllBySubjectId(Long subjectId);

    @Query("select t from TeacherAssignation t where t.id.teacherUserId = ?1")
    List<TeacherAssignation> findAllByTeacherUserId(Long teacherUserId);
}
