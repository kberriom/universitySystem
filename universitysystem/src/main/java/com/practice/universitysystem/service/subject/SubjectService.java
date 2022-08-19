package com.practice.universitysystem.service.subject;

import com.practice.universitysystem.dto.SubjectDto;
import com.practice.universitysystem.model.curriculum.subject.Subject;
import com.practice.universitysystem.model.users.student.student_subject.StudentSubjectRegistration;
import com.practice.universitysystem.model.users.student.student_subject.StudentSubjectRegistrationId;
import com.practice.universitysystem.model.users.teacher.teacher_asignation.TeacherAssignation;
import com.practice.universitysystem.model.users.teacher.teacher_asignation.TeacherAssignationId;
import com.practice.universitysystem.repository.curriculum.subject.SubjectRepository;
import com.practice.universitysystem.repository.users.student.student_subject.StudentSubjectRegistrationRepository;
import com.practice.universitysystem.repository.users.teacher.teacher_assignation.TeacherAssignationRepository;
import com.practice.universitysystem.service.ServiceUtils;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SubjectService {

    SubjectRepository subjectRepository;
    StudentSubjectRegistrationRepository registrationRepository;
    TeacherAssignationRepository teacherAssignationRepository;

    ServiceUtils<Subject, Long, SubjectRepository> subjectServiceUtils;
    ServiceUtils<StudentSubjectRegistration, StudentSubjectRegistrationId, StudentSubjectRegistrationRepository> registrationServiceUtils;
    ServiceUtils<TeacherAssignation, TeacherAssignationId, TeacherAssignationRepository> teacherAssignationServiceUtils;

    @Autowired
    public SubjectService(SubjectRepository subjectRepository, StudentSubjectRegistrationRepository registrationRepository, TeacherAssignationRepository teacherAssignationRepository) {
        this.registrationRepository = registrationRepository;
        this.teacherAssignationRepository = teacherAssignationRepository;
        this.subjectRepository = subjectRepository;
        subjectServiceUtils = new ServiceUtils<>(subjectRepository);
        registrationServiceUtils = new ServiceUtils<>(registrationRepository);
        teacherAssignationServiceUtils = new ServiceUtils<>(teacherAssignationRepository);
    }

    private static final SubjectMapper mapper = Mappers.getMapper(SubjectMapper.class);

    public Subject createSubject(SubjectDto subjectDto) {
        Subject subject = mapper.dtoToSubject(subjectDto);
        subjectServiceUtils.validate(subject);
        return subjectRepository.save(subject);
    }

    public Subject getSubject(String name) {
        return subjectRepository.findByName(name).orElseThrow();
    }

    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    public List<Object> getPaginatedSubjects(int page, int pageSize) {
        return subjectServiceUtils.getPaginatedList(page, pageSize);
    }

    public Subject updateSubject(String name, SubjectDto subjectDto) {
        Subject update = mapper.update(getSubject(name), subjectDto);
        subjectServiceUtils.validate(update);
        return subjectRepository.save(update);
    }

    public void deleteSubject(String name) {
        subjectRepository.delete(getSubject(name));
    }

    public List<StudentSubjectRegistration> getAllRegisteredStudents(String subjectName) {
        Subject subject = getSubject(subjectName);
        return registrationRepository.findAllBySubjectId(subject.getId());
    }

    public StudentSubjectRegistration addStudentToSubject(Long studentId, String subjectName) {
        StudentSubjectRegistration subjectRegistration =
                new StudentSubjectRegistration(studentId, getSubject(subjectName).getId());

        subjectRegistration.setRegistrationDate(LocalDate.now());

        registrationServiceUtils.validate(subjectRegistration);

        return registrationRepository.save(subjectRegistration);
    }

    public void removeStudent(Long studentId, String subjectName) {
        StudentSubjectRegistrationId registrationId = new StudentSubjectRegistrationId();
        registrationId.setStudentUserId(studentId);
        registrationId.setSubjectId(getSubject(subjectName).getId());
        registrationRepository.deleteById(registrationId);
    }

    public List<TeacherAssignation> getAllTeachers(String subjectName) {
        Subject subject = getSubject(subjectName);
        return teacherAssignationRepository.findAllBySubjectId(subject.getId());
    }

    public TeacherAssignation addTeacherToSubject(Long teacherId, String subjectName, String roleInClass) {
        TeacherAssignation teacherAssignation =
                new TeacherAssignation(teacherId, getSubject(subjectName).getId());

        teacherAssignation.setRoleInClass(roleInClass);

        teacherAssignationServiceUtils.validate(teacherAssignation);

        return teacherAssignationRepository.save(teacherAssignation);
    }

    public void removeTeacher(Long teacherId, String subjectName) {
        TeacherAssignationId assignationId = new TeacherAssignationId();
        assignationId.setTeacherUserId(teacherId);
        assignationId.setSubjectId(getSubject(subjectName).getId());
        teacherAssignationRepository.deleteById(assignationId);
    }

    public TeacherAssignation modifyTeacherRoleInSubject(Long teacherId, String subjectName, String roleInClass) {
        TeacherAssignationId assignationId = new TeacherAssignationId();
        assignationId.setTeacherUserId(teacherId);
        assignationId.setSubjectId(getSubject(subjectName).getId());
        TeacherAssignation teacherAssignation = teacherAssignationRepository.findById(assignationId).orElseThrow();
        teacherAssignation.setRoleInClass(roleInClass);
        teacherAssignationServiceUtils.validate(teacherAssignation);
        return teacherAssignationRepository.save(teacherAssignation);
    }

}
