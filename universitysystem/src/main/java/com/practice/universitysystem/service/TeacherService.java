package com.practice.universitysystem.service;

import com.practice.universitysystem.dto.PageInfoDto;
import com.practice.universitysystem.dto.teacher.TeacherDto;
import com.practice.universitysystem.dto.mapper.TeacherMapper;
import com.practice.universitysystem.dto.teacher.TeacherUpdateDto;
import com.practice.universitysystem.model.users.teacher.Teacher;
import com.practice.universitysystem.repository.users.teacher.TeacherRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.validation.*;
import java.util.*;

@Service
public class TeacherService {

    @Autowired
    private AuthService authService;
    @Autowired
    private TeacherRepository teacherRepository;

    private final TeacherMapper teacherMapper = Mappers.getMapper(TeacherMapper.class);

    public Teacher createTeacher(TeacherDto teacherDto) {
        String encodedPassword = authService.getEncodedPassword(teacherDto.getUserPassword());
        teacherDto.setUserPassword(encodedPassword);

        Teacher teacher = teacherMapper.dtoToTeacher(teacherDto);

        teacher.setEnrollmentDate(new Date());

        validateTeacher(teacher);

        return teacherRepository.save(teacher);
    }

    public void validateTeacher(Teacher teacher) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Teacher>> constraintViolations = validator.validate(teacher);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }

    public void deleteTeacher(String email) {
        Teacher teacher = getTeacher(email);
        teacherRepository.delete(teacher);
    }

    public void deleteTeacher(long id) {
        Teacher teacher = getTeacher(id);
        teacherRepository.delete(teacher);
    }

    public Teacher updateTeacher(String email, TeacherUpdateDto updateDto) {
        Teacher teacher = getTeacher(email);
        teacherMapper.update(teacher, updateDto);
        validateTeacher(teacher);
        return teacherRepository.save(teacher);
    }

    public Teacher updateTeacher(long id, TeacherUpdateDto updateDto) {
        Teacher teacher = getTeacher(id);
        teacherMapper.update(teacher, updateDto);
        validateTeacher(teacher);
        return teacherRepository.save(teacher);
    }

    public Teacher getTeacher(String email) {
        Optional<Teacher> teacher = teacherRepository.findByEmail(email);
        return teacher.orElseThrow();
    }

    public Teacher getTeacher(long id) {
        return teacherRepository.findById(id).orElseThrow();
    }

    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    public Page<Teacher> getAllTeachers(int page, int pageSize) {
        return teacherRepository.findAll(PageRequest.of(page, pageSize));
    }

    /**
     * @param page Desired page number, starts at 1
     * @param size total amount of pages
     * @return list containing current page info and the paginated list
     */
    public List<Object> getTeacherPaginatedList(int page, int size) {
        page--;

        if (page < 0) {
            throw new IllegalArgumentException("Page number must not be less than 1");
        }

        List<Teacher> teacherList = getAllTeachers(page, size).toList();
        int responseTeacherSize = teacherList.size();
        List<Object> responseList = new ArrayList<>(responseTeacherSize +1);

        PageInfoDto pageInfo = new PageInfoDto(page+1L, responseTeacherSize, maxPageNumberGiven(size));

        responseList.add(pageInfo);
        responseList.addAll(teacherList);
        return responseList;
    }

    private long maxPageNumberGiven(long pageSize) {
        long totalCount = teacherRepository.count();
        return (long) Math.ceil((double) totalCount / pageSize);
    }


}
