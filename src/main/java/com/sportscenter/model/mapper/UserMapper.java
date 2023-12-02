package com.sportscenter.model.mapper;

import com.sportscenter.model.binding.UserRegistrationBindingModel;
import com.sportscenter.model.entity.UserEntity;
import com.sportscenter.model.service.UserRegistrationServiceModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserRegistrationServiceModel userRegistrationBindingToUserRegistrationServiceModel(
            UserRegistrationBindingModel userRegistrationBindingModel);

    UserEntity userRegistrationServiceToUserEntity(UserRegistrationServiceModel userRegistrationServiceModel);
}
