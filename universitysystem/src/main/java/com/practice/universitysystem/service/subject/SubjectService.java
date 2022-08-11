package com.practice.universitysystem.service.subject;

import com.practice.universitysystem.dto.SubjectDto;
import com.practice.universitysystem.model.curriculum.subject.Subject;
import com.practice.universitysystem.model.users.student.Student;
import com.practice.universitysystem.model.users.student.student_subject.StudentSubjectRegistration;
import com.practice.universitysystem.model.users.student.student_subject.StudentSubjectRegistrationId;
import com.practice.universitysystem.repository.curriculum.subject.SubjectRepository;
import com.practice.universitysystem.repository.users.student.StudentRepository;
import com.practice.universitysystem.repository.users.student.student_subject.StudentSubjectRegistrationRepository;
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
    StudentRepository studentRepository;

    ServiceUtils<Subject, Long, SubjectRepository> subjectServiceUtils;
    ServiceUtils<StudentSubjectRegistration, StudentSubjectRegistrationId, StudentSubjectRegistrationRepository> registrationServiceUtils;

    @Autowired
    public SubjectService(SubjectRepository subjectRepository, StudentSubjectRegistrationRepository registrationRepository, StudentRepository studentRepository) {
        this.registrationRepository = registrationRepository;
        this.subjectRepository = subjectRepository;
        this.studentRepository = studentRepository;
        subjectServiceUtils = new ServiceUtils<>(subjectRepository);
        registrationServiceUtils = new ServiceUtils<>(registrationRepository);
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
        return subjectRepository.save(mapper.update(getSubject(name), subjectDto));
    }

    public void deleteSubject(String name) {
        subjectRepository.delete(getSubject(name));
    }

    public List<StudentSubjectRegistration> getAllRegisteredStudents(String subjectName) {
        Subject subject = getSubject(subjectName);
        return registrationRepository.findAllBySubjectId(subject.getId());
    }

    public StudentSubjectRegistration addStudentToSubject(Student student, String subjectName) {
        StudentSubjectRegistration subjectRegistration =
                new StudentSubjectRegistration(student.getId(), getSubject(subjectName).getId());

        subjectRegistration.setRegistrationDate(LocalDate.now());

        registrationServiceUtils.validate(subjectRegistration);

        return registrationRepository.save(subjectRegistration);
    }

    public void removeStudent(Student student, String subjectName) {
        StudentSubjectRegistrationId registrationId = new StudentSubjectRegistrationId();
        registrationId.setStudentUserId(student.getId());
        registrationId.setSubjectId(getSubject(subjectName).getId());
        registrationRepository.deleteById(registrationId);
    }

    public void getAllTeachers() {
        //todo
    }

    public void addTeacher() {
        //todo
    }

    public void removeTeacher() {
        //todo
    }

    public void modifyTeacherRoleInSubject() {
        //todo
    }


}
