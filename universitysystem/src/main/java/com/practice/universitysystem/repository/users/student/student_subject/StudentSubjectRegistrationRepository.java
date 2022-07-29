package com.practice.universitysystem.repository.users.student.student_subject;

import com.practice.universitysystem.model.users.student.student_subject.StudentSubjectRegistration;
import com.practice.universitysystem.model.users.student.student_subject.StudentSubjectRegistrationId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentSubjectRegistrationRepository extends JpaRepository<StudentSubjectRegistration, StudentSubjectRegistrationId> {
}
