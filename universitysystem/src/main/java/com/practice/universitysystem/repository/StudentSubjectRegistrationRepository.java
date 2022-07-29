package com.practice.universitysystem.repository;

import com.practice.universitysystem.model.student_subject.StudentSubjectRegistration;
import com.practice.universitysystem.model.student_subject.StudentSubjectRegistrationId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentSubjectRegistrationRepository extends JpaRepository<StudentSubjectRegistration, StudentSubjectRegistrationId> {
}
