package com.practice.universitysystem.repository;

import com.practice.universitysystem.model.Curriculum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurriculumRepository extends JpaRepository<Curriculum, Long> {
}
