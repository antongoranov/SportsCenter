package com.sportscenter.service;

import com.sportscenter.model.service.UserRegistrationServiceModel;
import com.sportscenter.model.view.UserProfileViewModel;

public interface UserService {

    void register(UserRegistrationServiceModel userRegistrationServiceModel);

    UserProfileViewModel getUserProfileByUsername(String username);
}
