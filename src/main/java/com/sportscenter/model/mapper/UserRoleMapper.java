package com.sportscenter.model.mapper;

import com.sportscenter.model.entity.UserRoleEntity;
import com.sportscenter.model.view.UserRoleViewModel;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UserRoleMapper {

    UserRoleViewModel userRoleEntityToViewModel(UserRoleEntity userRoleEntity);

}
