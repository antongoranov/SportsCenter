package com.sportscenter.service.impl;

import com.sportscenter.exception.UnableToProcessOperationException;
import com.sportscenter.exception.UserNotFoundException;
import com.sportscenter.model.entity.UserEntity;
import com.sportscenter.model.entity.UserRoleEntity;
import com.sportscenter.model.enums.UserRoleEnum;
import com.sportscenter.model.mapper.UserMapper;
import com.sportscenter.model.mapper.UserRoleMapper;
import com.sportscenter.model.service.UserPictureServiceModel;
import com.sportscenter.model.service.UserRegistrationServiceModel;
import com.sportscenter.model.view.UserProfileViewModel;
import com.sportscenter.model.view.UserRoleViewModel;
import com.sportscenter.model.view.UserViewModel;
import com.sportscenter.repository.UserRepository;
import com.sportscenter.repository.UserRoleRepository;
import com.sportscenter.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;
    private final PasswordEncoder passwordEncoder;

    private static final String UPLOAD_DIRECTORY =
            new File("src\\main\\resources\\static\\images\\users").getAbsolutePath();

    @Override
    public void register(UserRegistrationServiceModel userRegistrationServiceModel) {

        UserRoleEntity userRole =
                userRepository.count() == 0 ?
                        userRoleRepository.findByRole(UserRoleEnum.ADMIN) :
                        userRoleRepository.findByRole(UserRoleEnum.USER);

        UserEntity userEntity = userMapper.userRegistrationServiceToUserEntity(userRegistrationServiceModel);
        userEntity.setPassword(passwordEncoder.encode(userRegistrationServiceModel.getPassword()));
        userEntity.setRoles(Set.of(userRole));

        userRepository.save(userEntity);

        //login(userEntity.getUsername());

    }

    @Override
    public UserProfileViewModel getUserProfileByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::userEntityToProfileViewModel)
                .orElseThrow(() -> new UserNotFoundException("User with " + username + " does not exist!"));
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


    @Override
    public void changeProfilePic(UserPictureServiceModel userPictureServiceModel) throws IOException {

        MultipartFile filePicture = userPictureServiceModel.getFilePicture();

        UserEntity user = userRepository.findByUsername(userPictureServiceModel.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User with " + userPictureServiceModel.getUsername() + " does not exist!"));

        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, filePicture.getOriginalFilename());

        //overrides the file
        Files.write(fileNameAndPath, filePicture.getBytes());

        user.setProfilePictureUrl("/images/users/" + filePicture.getOriginalFilename());
        userRepository.save(user);
    }

    @Override
    public boolean userExists(String username) {
        return userRepository
                .findByUsername(username)
                .isPresent();
    }

    @Override
    public List<UserViewModel> getAllUsers() {
        return userRepository
                .findAll()
                .stream()
                .map(userMapper::mapUserEntityToViewModel)
                .collect(Collectors.toList());
    }

    @Override
    public UserViewModel getUserById(Long userId) {
        return userRepository
                .findById(userId)
                .map(userMapper::mapUserEntityToViewModel)
                .orElseThrow(() -> new UserNotFoundException("User with " + userId + " does not exist!"));
    }

    @Override
    public void updateUserRoles(Long userId, Set<UserRoleEntity> newRoles){

        UserEntity existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with " + userId + " does not exist!"));

        if(newRoles != null){
            existingUser.setRoles(newRoles);
            userRepository.save(existingUser);
        }
    }
}
