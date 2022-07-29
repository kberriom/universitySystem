package com.practice.universitysystem.repository.curriculum.subject;

import com.practice.universitysystem.model.curriculum.subject.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
}
