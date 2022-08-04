package com.practice.universitysystem.repository.users.teacher;

import com.practice.universitysystem.model.users.teacher.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    Optional<Teacher> findByEmail(String email);

}
