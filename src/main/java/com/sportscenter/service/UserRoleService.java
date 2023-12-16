package com.sportscenter.service;

import com.sportscenter.model.view.UserRoleViewModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

public interface UserRoleService {
    Set<UserRoleViewModel> findAll();
}
