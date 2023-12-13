package com.sportscenter.model.mapper;

import com.sportscenter.model.entity.InstructorEntity;
import com.sportscenter.model.view.InstructorViewModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InstructorMapper {

    @Mapping(target = "sportName", source = "sport.name" )
    @Mapping(target = "fullName", source = "fullName")
    InstructorViewModel mapInstructorEntityToViewModel(InstructorEntity instructorEntity);
}
