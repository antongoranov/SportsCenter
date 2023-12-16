package com.sportscenter.service.impl;

import com.sportscenter.model.mapper.UserRoleMapper;
import com.sportscenter.model.view.UserRoleViewModel;
import com.sportscenter.repository.UserRoleRepository;
import com.sportscenter.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository userRoleRepository;
    private final UserRoleMapper userRoleMapper;

    @Override
    public Set<UserRoleViewModel> findAll() {
        return userRoleRepository
                .findAll()
                .stream()
                .map(userRoleMapper::userRoleEntityToViewModel)
                .collect(Collectors.toSet());
    }
}
