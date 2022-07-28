package com.practice.universitysystem.repository;

import com.practice.universitysystem.model.StudentSubjectRegistration;
import com.practice.universitysystem.model.StudentSubjectRegistrationId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentSubjectRegistrationRepository extends JpaRepository<StudentSubjectRegistration, StudentSubjectRegistrationId> {
}
