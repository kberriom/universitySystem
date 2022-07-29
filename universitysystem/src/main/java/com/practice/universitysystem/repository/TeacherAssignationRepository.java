package com.practice.universitysystem.repository;

import com.practice.universitysystem.model.teacher_asignation.TeacherAssignation;
import com.practice.universitysystem.model.teacher_asignation.TeacherAssignationId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherAssignationRepository extends JpaRepository<TeacherAssignation, TeacherAssignationId> {
}
