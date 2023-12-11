package com.sportscenter.service;

import com.sportscenter.model.service.UserPictureServiceModel;
import com.sportscenter.model.service.UserRegistrationServiceModel;
import com.sportscenter.model.view.UserProfileViewModel;

import java.io.IOException;

public interface UserService {

    void register(UserRegistrationServiceModel userRegistrationServiceModel);

    UserProfileViewModel getUserProfileByUsername(String username);

    void changeProfilePic(UserPictureServiceModel userPictureServiceModel) throws IOException;

    boolean userExists(String username);
}
