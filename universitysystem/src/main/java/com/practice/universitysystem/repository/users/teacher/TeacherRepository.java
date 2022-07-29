package com.practice.universitysystem.repository.users.teacher;

import com.practice.universitysystem.model.users.teacher.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
}
