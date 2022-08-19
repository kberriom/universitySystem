package com.practice.universitysystem.service.grade;

import com.practice.universitysystem.dto.GradeDto;
import com.practice.universitysystem.model.curriculum.subject.Grade;
import com.practice.universitysystem.model.users.student.student_subject.StudentSubjectRegistration;
import com.practice.universitysystem.model.users.student.student_subject.StudentSubjectRegistrationId;
import com.practice.universitysystem.repository.curriculum.subject.GradeRepository;
import com.practice.universitysystem.repository.users.student.student_subject.StudentSubjectRegistrationRepository;
import com.practice.universitysystem.service.ServiceUtils;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class GradeService {

    GradeRepository gradeRepository;
    StudentSubjectRegistrationRepository registrationRepository;
    ServiceUtils<Grade, Long, GradeRepository> gradeServiceUtils;
    ServiceUtils<StudentSubjectRegistration, StudentSubjectRegistrationId, StudentSubjectRegistrationRepository> registrationServiceUtils;
    private final GradeMapper mapper = Mappers.getMapper(GradeMapper.class);

    public GradeService(GradeRepository gradeRepository, StudentSubjectRegistrationRepository registrationRepository) {
        this.gradeRepository = gradeRepository;
        this.registrationRepository = registrationRepository;
        gradeServiceUtils = new ServiceUtils<>(gradeRepository);
        registrationServiceUtils = new ServiceUtils<>(registrationRepository);
    }

    public StudentSubjectRegistration setStudentFinalGrade(Long subjectId, Long studentId, Double finalGrade) {
        StudentSubjectRegistration subjectRegistration = getStudentSubjectRegistration(subjectId, studentId);
        subjectRegistration.setFinalGrade(finalGrade);
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
        return registrationRepository.save(subjectRegistration);
    }

    private void verifyGradeCumulativePercentageOfFinalGrade(Set<Grade> grades, Grade grade) {
        double currentTotal = grades.stream().mapToDouble(Grade::getPercentageOfFinalGrade).sum();
        double futureTotal = grade.getPercentageOfFinalGrade() + currentTotal;
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

        verifyGradeCumulativePercentageOfFinalGrade(subjectRegistration.getSubjectGrades(), grade);

        if (subjectRegistration.getSubjectGrades() == null) {
            subjectRegistration.setSubjectGrades(new HashSet<>());
        }
        grade = gradeRepository.save(grade);
        subjectRegistration.getSubjectGrades().add(grade);
        return registrationRepository.save(subjectRegistration);
    }

    private StudentSubjectRegistration getStudentSubjectRegistration(Long subjectId, Long studentId) {
        StudentSubjectRegistrationId registrationId = new StudentSubjectRegistrationId();
        registrationId.setSubjectId(subjectId);
        registrationId.setStudentUserId(studentId);

        return registrationRepository.findById(registrationId).orElseThrow();
    }

    public StudentSubjectRegistration modifyStudentGrade(Long subjectId, Long studentId, Long gradeId, GradeDto gradeDto) {
        Grade grade = gradeRepository.findById(gradeId).orElseThrow();
        grade = mapper.update(grade, gradeDto);
        gradeServiceUtils.validate(grade);

        StudentSubjectRegistration subjectRegistration = getStudentSubjectRegistration(subjectId, studentId);

        subjectRegistration.getSubjectGrades().remove(gradeRepository.findById(gradeId).orElseThrow());

        verifyGradeCumulativePercentageOfFinalGrade(subjectRegistration.getSubjectGrades(), grade);

        gradeRepository.save(grade);
        subjectRegistration.getSubjectGrades().add(grade);

        return registrationRepository.save(subjectRegistration);
    }

    public void removeStudentGrade(Long subjectId, Long studentId, Long gradeId) {
        StudentSubjectRegistration subjectRegistration = getStudentSubjectRegistration(subjectId, studentId);
        Grade grade = gradeRepository.findById(gradeId).orElseThrow();

        subjectRegistration.getSubjectGrades().remove(grade);
        registrationRepository.save(subjectRegistration);

        gradeRepository.delete(grade);
    }

    public List<Set<Grade>> getAllStudentAllGradesInSubject(Long subjectId) {
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
