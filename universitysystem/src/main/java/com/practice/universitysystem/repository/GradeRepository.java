package com.practice.universitysystem.repository;

import com.practice.universitysystem.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeRepository extends JpaRepository<Grade, Long> {
}
