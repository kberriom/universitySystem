package com.practice.universitysystem.service.grade;

import com.practice.universitysystem.dto.GradeDto;
import com.practice.universitysystem.model.curriculum.subject.Grade;
import com.practice.universitysystem.model.users.student.student_subject.StudentSubjectRegistration;
import com.practice.universitysystem.model.users.student.student_subject.StudentSubjectRegistrationId;
import com.practice.universitysystem.repository.curriculum.subject.GradeRepository;
import com.practice.universitysystem.repository.curriculum.subject.SubjectRepository;
import com.practice.universitysystem.repository.users.student.student_subject.StudentSubjectRegistrationRepository;
import com.practice.universitysystem.service.ServiceUtils;
import com.practice.universitysystem.service.users.student.StudentService;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

@Service
public class GradeService {

    GradeRepository gradeRepository;
    SubjectRepository subjectRepository;
    StudentSubjectRegistrationRepository registrationRepository;
    StudentService studentService;
    ServiceUtils<Grade, Long, GradeRepository> gradeServiceUtils;
    ServiceUtils<StudentSubjectRegistration, StudentSubjectRegistrationId, StudentSubjectRegistrationRepository> registrationServiceUtils;
    private final GradeMapper mapper = Mappers.getMapper(GradeMapper.class);

    public GradeService(GradeRepository gradeRepository, StudentSubjectRegistrationRepository registrationRepository, SubjectRepository subjectRepository) {
        this.gradeRepository = gradeRepository;
        this.subjectRepository = subjectRepository;
        this.registrationRepository = registrationRepository;
        gradeServiceUtils = new ServiceUtils<>(gradeRepository);
        registrationServiceUtils = new ServiceUtils<>(registrationRepository);
    }

    public StudentSubjectRegistration setStudentFinalGrade(Long subjectId, Long studentId, Double finalGrade) {
        StudentSubjectRegistration subjectRegistration = getStudentSubjectRegistration(subjectId, studentId);
        subjectRegistration.setFinalGrade(finalGrade);
        registrationServiceUtils.validate(subjectRegistration);
        return registrationRepository.save(subjectRegistration);
    }

    private double percentGrade(double grade) {
        return ((100D)*(grade))/(5D);
    }

    public StudentSubjectRegistration setStudentFinalGradeAuto(Long subjectId, Long studentId) {
        StudentSubjectRegistration subjectRegistration = getStudentSubjectRegistration(subjectId, studentId);
        Set<Grade> grades = subjectRegistration.getSubjectGrades();
        double finalGrade;

        if (grades.stream().mapToDouble(Grade::getPercentageOfFinalGrade).sum() < 100) {
            throw new IllegalStateException("Cumulative percentage of final grade cannot be less than 100%");
        }

        double finalGradePercentage = grades.stream()
                .mapToDouble(value -> percentGrade(value.getGradeValue()) * value.getPercentageOfFinalGrade() ).sum() / 100;

        finalGrade = (5) * (finalGradePercentage) / (100);

        subjectRegistration.setFinalGrade(finalGrade);
        registrationServiceUtils.validate(subjectRegistration);
        return registrationRepository.save(subjectRegistration);
    }

    private void verifyGradeCumulativePercentageOfFinalGrade(Set<Grade> grades, double newGradePercentageOfFinalGrade) {
        double currentTotal = grades.stream().mapToDouble(Grade::getPercentageOfFinalGrade).sum();
        double futureTotal = newGradePercentageOfFinalGrade + currentTotal;
        if (futureTotal > 100D) {
            throw new IllegalArgumentException("Grade percentage of final grade would excede 100%");
        }
    }

    public StudentSubjectRegistration addStudentGrade(Long subjectId, Long studentId, GradeDto gradeDto) {
        Grade grade = mapper.dtoToGrade(gradeDto);
        StudentSubjectRegistrationId registrationId = new StudentSubjectRegistrationId();
        registrationId.setStudentUserId(studentId);
        registrationId.setSubjectId(subjectId);
        grade.setRegistrationId(registrationId);
        gradeServiceUtils.validate(grade);

        StudentSubjectRegistration subjectRegistration = getStudentSubjectRegistration(subjectId, studentId);

        if (subjectRegistration.getSubjectGrades() == null) {
            subjectRegistration.setSubjectGrades(new HashSet<>());
        }
        
        verifyGradeCumulativePercentageOfFinalGrade(subjectRegistration.getSubjectGrades(), grade.getPercentageOfFinalGrade());

        grade = gradeRepository.save(grade);
        subjectRegistration.getSubjectGrades().add(grade);
        return registrationRepository.save(subjectRegistration);
    }

    public StudentSubjectRegistration getStudentSubjectRegistration(Long subjectId, Long studentId) {
        StudentSubjectRegistrationId registrationId = new StudentSubjectRegistrationId();
        registrationId.setSubjectId(subjectId);
        registrationId.setStudentUserId(studentId);

        return registrationRepository.findById(registrationId).orElseThrow(() ->
                new NoSuchElementException("Could not find a registration with Student id: " + studentId + " and Subject id: " + subjectId));
    }

    private Grade getGradeById(Long gradeId) {
        return gradeRepository.findById(gradeId).orElseThrow(() ->
                new NoSuchElementException("Could not find a grade with id: " + gradeId));
    }

    @Transactional(rollbackFor = Exception.class)
    public StudentSubjectRegistration modifyStudentGrade(Long subjectId, Long studentId, Long gradeId, GradeDto gradeDto) {
        Grade grade = getGradeById(gradeId);
        StudentSubjectRegistration subjectRegistration = getStudentSubjectRegistration(subjectId, studentId);

        Set<Grade> grades = Optional.of(subjectRegistration.getSubjectGrades()).orElseThrow(()->
                new NoSuchElementException("Unable to find any existing grades in " + subjectRegistration.getId()));

        if (!grades.contains(grade)) {
            throw new NoSuchElementException("Unable to find a grade with id: " + gradeId + " in the given student registration with studentId: " + studentId);
        }

        double gradePercentage;
        if (gradeDto.getPercentageOfFinalGrade() == null) {
            gradePercentage = grade.getPercentageOfFinalGrade();
        } else {
            gradePercentage = gradeDto.getPercentageOfFinalGrade();
        }
        subjectRegistration.getSubjectGrades().remove(grade);
        verifyGradeCumulativePercentageOfFinalGrade(subjectRegistration.getSubjectGrades(), gradePercentage);

        grade = mapper.update(grade, gradeDto);
        gradeServiceUtils.validate(grade);
        gradeRepository.save(grade);
        gradeRepository.flush();

        subjectRegistration.getSubjectGrades().add(grade);
        return registrationRepository.save(subjectRegistration);
    }

    public void removeStudentGrade(Long subjectId, Long studentId, Long gradeId) {
        StudentSubjectRegistration subjectRegistration = getStudentSubjectRegistration(subjectId, studentId);
        Grade grade = getGradeById(gradeId);

        subjectRegistration.getSubjectGrades().remove(grade);
        registrationRepository.save(subjectRegistration);

        gradeRepository.delete(grade);
    }

    public List<Set<Grade>> getAllStudentAllGradesInSubject(Long subjectId) {
        if (!subjectRepository.existsById(subjectId)) {
            throw new NoSuchElementException("Could not find a subject with subjectId: " + subjectId);
        }
        return registrationRepository.findAllBySubjectId(subjectId)
                .stream().map(StudentSubjectRegistration::getSubjectGrades).toList();
    }

    public List<Set<Grade>> getAllStudentAllGrades() {
        return registrationRepository.findAll().stream().map(StudentSubjectRegistration::getSubjectGrades).toList();
    }

    public Set<Grade> getOneStudentGrades(Long subjectId, Long studentId) {
        StudentSubjectRegistration subjectRegistration = getStudentSubjectRegistration(subjectId, studentId);
        return subjectRegistration.getSubjectGrades();
    }
}
