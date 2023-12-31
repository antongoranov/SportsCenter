package com.sportscenter.model.mapper;

import com.sportscenter.model.binding.UserEditBindingModel;
import com.sportscenter.model.binding.UserRegistrationBindingModel;
import com.sportscenter.model.entity.UserEntity;
import com.sportscenter.model.service.UserEditServiceModel;
import com.sportscenter.model.service.UserRegistrationServiceModel;
import com.sportscenter.model.view.UserProfileViewModel;
import com.sportscenter.model.view.UserViewModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = BookingMapper.class)
public interface UserMapper {

    UserRegistrationServiceModel userRegistrationBindingToUserRegistrationServiceModel(
            UserRegistrationBindingModel userRegistrationBindingModel);

    UserEntity userRegistrationServiceToUserEntity(UserRegistrationServiceModel userRegistrationServiceModel);

    @Mapping(target = "bookings", source = "bookings")
    UserProfileViewModel userEntityToProfileViewModel(UserEntity userEntity);

    UserViewModel mapUserEntityToViewModel(UserEntity userEntity);

    UserEditBindingModel mapUserViewToBindingModel(UserViewModel userViewModel);

    UserEditServiceModel userEditBindingModelToService(UserEditBindingModel userEditBindingModel);
}
