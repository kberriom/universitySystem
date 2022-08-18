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
import com.practice.universitysystem.service.users.student.StudentService;
import com.practice.universitysystem.service.users.teacher.TeacherService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class SubjectService {

    SubjectRepository subjectRepository;
    StudentSubjectRegistrationRepository registrationRepository;
    TeacherAssignationRepository teacherAssignationRepository;
    StudentService studentService;
    TeacherService teacherService;

    ServiceUtils<Subject, Long, SubjectRepository> subjectServiceUtils;
    ServiceUtils<StudentSubjectRegistration, StudentSubjectRegistrationId, StudentSubjectRegistrationRepository> registrationServiceUtils;
    ServiceUtils<TeacherAssignation, TeacherAssignationId, TeacherAssignationRepository> teacherAssignationServiceUtils;

    @Autowired
    public SubjectService(SubjectRepository subjectRepository, StudentSubjectRegistrationRepository registrationRepository
            , TeacherAssignationRepository teacherAssignationRepository, StudentService studentService, TeacherService teacherService) {
        this.registrationRepository = registrationRepository;
        this.teacherAssignationRepository = teacherAssignationRepository;
        this.subjectRepository = subjectRepository;
        this.studentService = studentService;
        this.teacherService = teacherService;
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
        return subjectRepository.findByName(name).
                orElseThrow(()->new NoSuchElementException("Unable to find a Subject with name: " + name));
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

        studentService.getUser(studentId);
        long subjectId = getSubject(subjectName).getId();

        StudentSubjectRegistrationId posibleExistingRegistration = new StudentSubjectRegistrationId();
        posibleExistingRegistration.setSubjectId(subjectId);
        posibleExistingRegistration.setStudentUserId(studentId);
        Optional<StudentSubjectRegistration> posibleRegistration = registrationRepository.findById(posibleExistingRegistration);
        if (posibleRegistration.isPresent()) {
            throw new IllegalStateException("Unable to create a new registration, registration already exists! " + posibleExistingRegistration);
        }

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
        StudentSubjectRegistration subjectRegistration = registrationRepository.findById(registrationId).
                orElseThrow(()-> new NoSuchElementException("Unable to find Student registration with " + registrationId));
        registrationRepository.delete(subjectRegistration);
    }

    public List<TeacherAssignation> getAllTeachers(String subjectName) {
        Subject subject = getSubject(subjectName);
        return teacherAssignationRepository.findAllBySubjectId(subject.getId());
    }

    public TeacherAssignation addTeacherToSubject(Long teacherId, String subjectName, String roleInClass) {

        teacherService.getUser(teacherId);

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
        TeacherAssignation teacherAssignation = teacherAssignationRepository.findById(assignationId).
                orElseThrow(()->new NoSuchElementException("Unable to find a teacher registration with " + assignationId));
        teacherAssignationRepository.delete(teacherAssignation);
    }

    public TeacherAssignation modifyTeacherRoleInSubject(Long teacherId, String subjectName, String roleInClass) {
        TeacherAssignationId assignationId = new TeacherAssignationId();
        assignationId.setTeacherUserId(teacherId);
        assignationId.setSubjectId(getSubject(subjectName).getId());
        TeacherAssignation teacherAssignation = teacherAssignationRepository.findById(assignationId).
                orElseThrow(()->new NoSuchElementException("Unable to find a teacher registration with " + assignationId));
        teacherAssignation.setRoleInClass(roleInClass);
        teacherAssignationServiceUtils.validate(teacherAssignation);
        return teacherAssignationRepository.save(teacherAssignation);
    }

}
