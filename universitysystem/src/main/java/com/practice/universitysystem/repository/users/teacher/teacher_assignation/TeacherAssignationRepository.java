package com.practice.universitysystem.repository.users.teacher.teacher_assignation;

import com.practice.universitysystem.model.users.teacher.teacher_asignation.TeacherAssignation;
import com.practice.universitysystem.model.users.teacher.teacher_asignation.TeacherAssignationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeacherAssignationRepository extends JpaRepository<TeacherAssignation, TeacherAssignationId> {

    @Query("from TeacherAssignation where subject_id=:subjectId")
    List<TeacherAssignation> findAllBySubjectId(@Param("subjectId") Long subjectId);

    @Query("from TeacherAssignation where teacher_user_id=:teacherUserId")
    List<TeacherAssignation> findAllByTeacherUserId(@Param("teacherUserId") Long teacherUserId);
}
