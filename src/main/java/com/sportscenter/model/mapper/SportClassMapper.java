package com.sportscenter.model.mapper;

import com.sportscenter.model.entity.SportClassEntity;
import com.sportscenter.model.view.SportClassViewModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SportClassMapper {

    @Mapping(target = "sportId", source = "sport.id" )
    @Mapping(target = "sportName", source = "sport.name" )
    @Mapping(target = "instructorName", source = "instructor.fullName")

    //for the testing purposes
    //@Mapping(target = "dayOfWeek", source = "dayOfWeek.value")
    //@Mapping(target = "startHour", source = "startTime.hour")
    //@Mapping(target = "endHour", source = "endTime.hour")

    SportClassViewModel sportClassEntityToViewModel(SportClassEntity sportClassEntity);
}
