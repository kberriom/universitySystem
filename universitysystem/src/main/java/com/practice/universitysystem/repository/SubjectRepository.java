package com.practice.universitysystem.repository;

import com.practice.universitysystem.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
}
