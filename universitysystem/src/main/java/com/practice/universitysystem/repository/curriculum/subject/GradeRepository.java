package com.practice.universitysystem.repository.curriculum.subject;

import com.practice.universitysystem.model.curriculum.subject.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeRepository extends JpaRepository<Grade, Long> {
}
