package com.sportscenter.service;

import com.sportscenter.model.entity.UserRoleEntity;
import com.sportscenter.model.service.UserPictureServiceModel;
import com.sportscenter.model.service.UserRegistrationServiceModel;
import com.sportscenter.model.view.UserProfileViewModel;
import com.sportscenter.model.view.UserRoleViewModel;
import com.sportscenter.model.view.UserViewModel;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface UserService {

    void register(UserRegistrationServiceModel userRegistrationServiceModel);

    UserProfileViewModel getUserProfileByUsername(String username);

    void changeProfilePic(UserPictureServiceModel userPictureServiceModel) throws IOException;

    boolean userExists(String username);

    List<UserViewModel> getAllUsers();

    UserViewModel getUserById(Long userId);

    void updateUserRoles(Long userId, Set<UserRoleEntity> roles);

    void deleteUserById(Long userId);
}
