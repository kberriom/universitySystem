package com.practice.universitysystem.repository.curriculum;

import com.practice.universitysystem.model.curriculum.Curriculum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurriculumRepository extends JpaRepository<Curriculum, Long> {
}
