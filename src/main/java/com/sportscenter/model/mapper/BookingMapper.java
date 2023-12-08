package com.sportscenter.model.mapper;

import com.sportscenter.model.entity.BookingEntity;
import com.sportscenter.model.enums.BookingStatusEnum;
import com.sportscenter.model.view.BookingViewModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "sportClass", target = "sportClass")
    @Mapping(source = "status", target = "status", qualifiedByName = "enumToString")
    @Mapping(source = "status", target = "statusNotActive", qualifiedByName = "isStatusNotActiveToBoolean")
    BookingViewModel bookingEntityToViewModel(BookingEntity bookingEntity);

    @Named("enumToString")
    default String enumToString(BookingStatusEnum status) {
        return status != null ? status.name() : null;
    }

    @Named("isStatusNotActiveToBoolean")
    default boolean isStatusNotActiveToBoolean(BookingStatusEnum status){
        return status != BookingStatusEnum.ACTIVE;
    }
}
