package com.sportscenter.service.impl;

import com.sportscenter.model.entity.UserEntity;
import com.sportscenter.model.entity.UserRoleEntity;
import com.sportscenter.model.enums.UserRoleEnum;
import com.sportscenter.model.mapper.UserMapper;
import com.sportscenter.model.service.UserRegistrationServiceModel;
import com.sportscenter.repository.UserRepository;
import com.sportscenter.repository.UserRoleRepository;
import com.sportscenter.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService appUserDetailsService;

    @Override
    public void register(UserRegistrationServiceModel userRegistrationServiceModel) {

        UserRoleEntity userRole =
                userRepository.count() == 0 ?
                        userRoleRepository.findByRole(UserRoleEnum.ADMIN) :
                        userRoleRepository.findByRole(UserRoleEnum.USER);

        UserEntity userEntity =  userMapper.userRegistrationServiceToUserEntity(userRegistrationServiceModel);
        userEntity.setPassword(passwordEncoder.encode(userRegistrationServiceModel.getPassword()));
        userEntity.setRoles(Set.of(userRole));

        userRepository.save(userEntity);

        //login(userEntity.getUsername());

    }

    //after redirection the context is set to AnonymousToken
//    private void login(String username) {
//        UserDetails userDetails = appUserDetailsService.loadUserByUsername(username);
//
//        Authentication auth = new UsernamePasswordAuthenticationToken(
//                userDetails,
//                userDetails.getPassword(),
//                userDetails.getAuthorities());
//
//        SecurityContextHolder.getContext().setAuthentication(auth);
//    }
}
