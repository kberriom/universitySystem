package com.practice.universitysystem.service.curriculum;

import com.practice.universitysystem.dto.CurriculumDto;
import com.practice.universitysystem.model.curriculum.Curriculum;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper
public interface CurriculumMapper {

    CurriculumDto curriculumToDTO(Curriculum curriculum);

    Curriculum dtoToCurriculum(CurriculumDto curriculumDto);

    @BeanMapping(nullValuePropertyMappingStrategy =  NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    Curriculum update(@MappingTarget Curriculum curriculum, CurriculumDto curriculumDto);

}
