package com.practice.universitysystem.repository.users.teacher.teacher_assignation;

import com.practice.universitysystem.model.users.teacher.teacher_asignation.TeacherAssignation;
import com.practice.universitysystem.model.users.teacher.teacher_asignation.TeacherAssignationId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherAssignationRepository extends JpaRepository<TeacherAssignation, TeacherAssignationId> {
}
