package com.sportscenter.service.impl;

import com.sportscenter.exception.UserNotFoundException;
import com.sportscenter.model.entity.BookingEntity;
import com.sportscenter.model.entity.PasswordResetToken;
import com.sportscenter.model.entity.UserEntity;
import com.sportscenter.model.entity.UserRoleEntity;
import com.sportscenter.model.enums.UserRoleEnum;
import com.sportscenter.model.mapper.UserMapper;
import com.sportscenter.model.service.PasswordChangeServiceModel;
import com.sportscenter.model.service.UserEditServiceModel;
import com.sportscenter.model.service.UserPictureServiceModel;
import com.sportscenter.model.service.UserRegistrationServiceModel;
import com.sportscenter.model.view.UserProfileViewModel;
import com.sportscenter.model.view.UserViewModel;
import com.sportscenter.repository.PasswordResetTokenRepository;
import com.sportscenter.repository.UserRepository;
import com.sportscenter.repository.UserRoleRepository;
import com.sportscenter.service.BookingService;
import com.sportscenter.service.CloudinaryService;
import com.sportscenter.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository pwResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final BookingService bookingService;
    private final EmailService emailService;
    private final UserDetailsService userDetailsService;
    private final CloudinaryService cloudinaryService;

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

        emailService.sendRegistrationEmail(userEntity.getEmail(), userEntity.getUsername());
    }

    @Override
    public UserProfileViewModel getUserProfileByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::userEntityToProfileViewModel)
                .orElseThrow(() -> new UserNotFoundException("User with username: " + username + " does not exist!"));
    }

    @Override
    public void changeProfilePic(UserPictureServiceModel userPictureServiceModel) throws IOException {

        MultipartFile filePicture = userPictureServiceModel.getFilePicture();

        UserEntity user = userRepository.findByUsername(userPictureServiceModel.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User with username: " + userPictureServiceModel.getUsername() + " does not exist!"));

        String userProfilePictureUrl = user.getProfilePictureUrl();
        if (userProfilePictureUrl != null) {
            String publicId =
                    userProfilePictureUrl.substring(61, userProfilePictureUrl.lastIndexOf('.'));

            cloudinaryService.deleteUserOldProfilePicture(publicId);
        }

        String newPictureUrl =
                cloudinaryService.uploadUserProfilePicture(filePicture);

        user.setProfilePictureUrl(newPictureUrl);
        userRepository.save(user);
    }

    @Override
    public boolean userExists(String username) {
        return userRepository
                .findByUsername(username)
                .isPresent();
    }

    @Override
    public boolean userWithEmailExists(String email) {
        return userRepository
                .findByEmail(email)
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
                .orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " does not exist!"));
    }

    @Override
    public UserEntity getUserByEmail(String email) {
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email: " + email + " does not exist!"));
    }

    @Override
    public void updateUserRoles(Long userId, Set<UserRoleEntity> newRoles) {

        UserEntity existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " does not exist!"));

        if (newRoles != null) {
            existingUser.setRoles(newRoles);
            userRepository.save(existingUser);
        }
    }

    @Override
    public void deleteUserById(Long userId) {

        UserEntity existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " does not exist!"));

        List<BookingEntity> activeBookings = bookingService.getActiveBookingsByUser(existingUser);

        activeBookings.forEach(booking -> {
            bookingService.cancelBooking(booking.getId());
        });

        userRepository.delete(existingUser);
    }

    @Override
    public void editUserData(Long userId, UserEditServiceModel userEditServiceModel) {

        UserEntity existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " does not exist!"));

        existingUser.setFirstName(userEditServiceModel.getFirstName());
        existingUser.setLastName(userEditServiceModel.getLastName());
        existingUser.setEmail(userEditServiceModel.getEmail());
        existingUser.setUsername(userEditServiceModel.getUsername());

        userRepository.save(existingUser);

    }

    //EDIT PROFILE INFO VALIDATIONS
    @Override
    public boolean isLoggedUserTheAccountHolder(String principalUsername, Long userId) {

        //method called from endpoint reached only when authenticated
        UserEntity loggedUser = userRepository.findByUsername(principalUsername).get();

        UserEntity editedUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " does not exist!"));

        return loggedUser.getId().equals(editedUser.getId());
    }

    @Override
    public boolean isNewEmailDifferentAndExisting(Long userId, String newEmail) {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " does not exist!"));

        return !user.getEmail().equals(newEmail) &&
                userRepository.findByEmail(newEmail).isPresent();
    }

    @Override
    public boolean isNewUsernameDifferentAndExisting(Long userId, String newUsername) {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " does not exist!"));

        return !user.getUsername().equals(newUsername) &&
                userRepository.findByUsername(newUsername).isPresent();
    }

    @Override
    public void changeUserPassword(PasswordChangeServiceModel pwChangeServiceModel) {

        //will always have a valid token when reached
        PasswordResetToken pwResetToken = pwResetTokenRepository.findByToken(pwChangeServiceModel.getToken()).get();

        UserEntity user = pwResetToken.getUser();
        user.setPassword(passwordEncoder.encode(pwChangeServiceModel.getNewPassword()));

        userRepository.save(user);
    }

    @Override
    public void createUserIfNotExist(String email) {
        Optional<UserEntity> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {

            UserRegistrationServiceModel newUser =
                    UserRegistrationServiceModel.builder()
                            .firstName("New")
                            .lastName("User")
                            .email(email)
                            .username(email)
                            .password(UUID.randomUUID().toString())
                            .build();

            register(newUser);
        }
    }

    @Override
    public void login(String username) {

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(username);

        Authentication auth =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        userDetails.getPassword(),
                        userDetails.getAuthorities());

        SecurityContextHolder.
                getContext().
                setAuthentication(auth);
    }
}
