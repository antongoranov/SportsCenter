package com.sportscenter.model.view;

import com.sportscenter.model.enums.UserRoleEnum;
import lombok.Data;

@Data
public class UserRoleViewModel {

    private Long id;
    private UserRoleEnum role;

}
