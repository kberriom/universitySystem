package com.practice.universitysystem.repository.users.student.student_subject;

import com.practice.universitysystem.model.users.student.student_subject.StudentSubjectRegistration;
import com.practice.universitysystem.model.users.student.student_subject.StudentSubjectRegistrationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentSubjectRegistrationRepository extends JpaRepository<StudentSubjectRegistration, StudentSubjectRegistrationId> {

    @Query("from StudentSubjectRegistration where subject_id=:subjectId")
    List<StudentSubjectRegistration> findAllBySubjectId(@Param("subjectId") Long subjectId);

}
