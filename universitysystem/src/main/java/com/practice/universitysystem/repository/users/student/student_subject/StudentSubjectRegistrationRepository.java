package com.practice.universitysystem.repository.users.student.student_subject;

import com.practice.universitysystem.model.users.student.student_subject.StudentSubjectRegistration;
import com.practice.universitysystem.model.users.student.student_subject.StudentSubjectRegistrationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentSubjectRegistrationRepository extends JpaRepository<StudentSubjectRegistration, StudentSubjectRegistrationId> {

    @Query("select s from StudentSubjectRegistration s where s.id.subjectId = ?1")
    List<StudentSubjectRegistration> findAllBySubjectId(Long subjectId);

    @Query("select s from StudentSubjectRegistration s where s.id.studentUserId = ?1")
    List<StudentSubjectRegistration> findAllByStudentUserId(Long studentUserId);
}
