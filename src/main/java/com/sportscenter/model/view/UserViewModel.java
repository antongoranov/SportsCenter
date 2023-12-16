package com.sportscenter.model.view;

import com.sportscenter.model.entity.UserRoleEntity;
import lombok.Data;

import java.util.Set;

@Data
public class UserViewModel {

    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private Set<UserRoleEntity> roles;

//    private Set<UserRoleViewModel> roles;
//    private Set<Long> roleIds;
}
