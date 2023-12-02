package com.sportscenter.service;

import com.sportscenter.model.entity.UserEntity;
import com.sportscenter.model.service.UserRegistrationServiceModel;
import org.springframework.security.core.Authentication;

public interface UserService {

    void registerAndLogin(UserRegistrationServiceModel userRegistrationServiceModel);

}
