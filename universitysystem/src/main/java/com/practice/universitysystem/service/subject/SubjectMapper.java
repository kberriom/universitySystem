package com.practice.universitysystem.service.subject;

import com.practice.universitysystem.dto.SubjectDto;
import com.practice.universitysystem.model.curriculum.subject.Subject;
import org.mapstruct.*;

@Mapper
public interface SubjectMapper {

    Subject dtoToSubject(SubjectDto subjectDto);

    SubjectDto subjectToDTO(Subject subject);

    @BeanMapping(nullValuePropertyMappingStrategy =  NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    Subject update(@MappingTarget Subject subject, SubjectDto subjectDto);

}
