package com.sportscenter.service;

import com.sportscenter.model.service.UserPictureServiceModel;
import com.sportscenter.model.service.UserRegistrationServiceModel;
import com.sportscenter.model.view.UserProfileViewModel;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {

    void register(UserRegistrationServiceModel userRegistrationServiceModel);

    UserProfileViewModel getUserProfileByUsername(String username);

    void changeProfilePic(UserPictureServiceModel userPictureServiceModel) throws IOException;
}
