package com.sportscenter.model.mapper;

import com.sportscenter.model.entity.SportEntity;
import com.sportscenter.model.view.SportViewModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SportMapper {

    SportViewModel sportEntityToViewModel(SportEntity sport);
}
