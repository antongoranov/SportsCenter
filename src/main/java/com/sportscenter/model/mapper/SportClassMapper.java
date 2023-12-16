package com.sportscenter.model.mapper;

import com.sportscenter.model.binding.AddSportClassBindingModel;
import com.sportscenter.model.entity.SportClassEntity;
import com.sportscenter.model.service.AddSportClassServiceModel;
import com.sportscenter.model.view.SportClassBookingViewModel;
import com.sportscenter.model.view.SportClassViewModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface SportClassMapper {

    @Mapping(target = "sportName", source = "sport.name" )
    @Mapping(target = "instructorName", source = "instructor.fullName")
    SportClassViewModel sportClassEntityToViewModel(SportClassEntity sportClassEntity);

    @Mapping(target = "sportId", source = "sport.id" )
    @Mapping(target = "sportName", source = "sport.name" )
    @Mapping(target = "sportImageUrl", source = "sport.sportImageUrl")
    @Mapping(target = "sportDescription", source = "sport.description")
    @Mapping(target = "instructorName", source = "instructor.fullName")
//    @Mapping(target = "startTime",
//            expression = "java(java.time.LocalTime.parse(sportClassEntity.getStartTime().format(java.time.format.DateTimeFormatter.ofPattern(\"HH:mm\"))))")
    SportClassBookingViewModel sportClassEntityToBookingViewModel(SportClassEntity sportClassEntity);

    AddSportClassServiceModel mapAddSportClassBindingModelToAddSportClassService(AddSportClassBindingModel addSportClassBindingModel);
}
