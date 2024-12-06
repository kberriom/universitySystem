package com.practice.universitysystem.repository.curriculum.subject;

import com.practice.universitysystem.model.curriculum.subject.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

    Optional<Subject> findByName(String name);

    @Query("select s from Subject s join TeacherAssignation a on s.id = a.id.subjectId where a.id.teacherUserId = ?1")
    List<Subject> findAllSubjectsByTeacherId(Long userId);

    @Query("select s from Subject s join StudentSubjectRegistration r on s.id = r.id.subjectId where r.id.studentUserId = ?1")
    List<Subject> findAllByStudentId(Long userId);

}
