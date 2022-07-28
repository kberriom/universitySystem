package com.practice.universitysystem.repository;

import com.practice.universitysystem.model.studentsubject.StudentSubjectRegistration;
import com.practice.universitysystem.model.studentsubject.StudentSubjectRegistrationId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentSubjectRegistrationRepository extends JpaRepository<StudentSubjectRegistration, StudentSubjectRegistrationId> {
}
