package com.practice.universitysystem.service.grade;

import com.practice.universitysystem.dto.GradeDto;
import com.practice.universitysystem.model.curriculum.subject.Grade;
import org.mapstruct.*;

@Mapper
public interface GradeMapper {

    GradeDto gradeToDTO(Grade grade);

    Grade dtoToGrade(GradeDto gradeDto);

    @BeanMapping(nullValuePropertyMappingStrategy =  NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    Grade update(@MappingTarget Grade grade, GradeDto gradeDto);

}
