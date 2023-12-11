package com.sportscenter.model.binding;

import lombok.Data;

@Data
public class UserSearchBindingModel {

    private String username;

    public boolean isEmpty(){
        return username == null || username.isEmpty();
    }
}
