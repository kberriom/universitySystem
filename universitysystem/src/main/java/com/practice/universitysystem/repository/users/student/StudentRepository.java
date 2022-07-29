package com.practice.universitysystem.repository.users.student;

import com.practice.universitysystem.model.users.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
