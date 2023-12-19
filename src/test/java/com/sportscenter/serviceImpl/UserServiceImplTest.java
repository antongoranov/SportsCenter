package com.sportscenter.serviceImpl;

import com.sportscenter.exception.UserNotFoundException;
import com.sportscenter.model.entity.BookingEntity;
import com.sportscenter.model.entity.UserEntity;
import com.sportscenter.model.entity.UserRoleEntity;
import com.sportscenter.model.enums.UserRoleEnum;
import com.sportscenter.model.mapper.UserMapper;
import com.sportscenter.model.service.UserEditServiceModel;
import com.sportscenter.model.service.UserPictureServiceModel;
import com.sportscenter.model.service.UserRegistrationServiceModel;
import com.sportscenter.model.view.UserProfileViewModel;
import com.sportscenter.model.view.UserViewModel;
import com.sportscenter.repository.UserRepository;
import com.sportscenter.repository.UserRoleRepository;
import com.sportscenter.service.BookingService;
import com.sportscenter.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRoleRepository userRoleRepositoryMock;
    @Mock
    private UserRepository userRepositoryMock;
    @Mock
    private PasswordEncoder passwordEncoderMock;
    @Mock
    private UserMapper userMapperMock;
    @Mock
    private BookingService bookingServiceMock;

    @InjectMocks
    private UserServiceImpl userServiceTest;

    private UserRoleEntity testAdminRole;
    private UserRoleEntity testUserRole;

    private UserEntity testAdminEntity;
    private UserEntity testUserEntity;
    private static final Long userId = 1L;
    private static final Long adminId = 2L;
    private static final Long userIdFake = 3L;

    @BeforeEach
    public void setUp() {
        testAdminRole = new UserRoleEntity(UserRoleEnum.ADMIN);
        testUserRole = new UserRoleEntity(UserRoleEnum.USER);

        testAdminEntity = UserEntity.builder()
                .firstName("User")
                .lastName("Userov")
                .username("user")
                .email("user@email.com")
                .password("12345")
                .profilePictureUrl("url/testUser")
                .roles(Set.of(testAdminRole, testUserRole))
                .build();
        testAdminEntity.setId(adminId);

        testUserEntity = UserEntity.builder()
                .firstName("User")
                .lastName("Userov")
                .username("user")
                .email("user@email.com")
                .password("12345")
                .profilePictureUrl("url/testUser")
                .roles(Set.of(testUserRole))
                .build();
        testUserEntity.setId(userId);
    }


    //register
    @Test
    public void testRegister_Admin() {
        UserRegistrationServiceModel testUserRegModel = UserRegistrationServiceModel.builder()
                .firstName("User")
                .lastName("Userov")
                .username("user")
                .email("user@email.com")
                .password("12345")
                .build();

        when(userRepositoryMock.count())
                .thenReturn(0L);

        when(userRoleRepositoryMock.findByRole(UserRoleEnum.ADMIN))
                .thenReturn(testAdminRole);

        when(userMapperMock.userRegistrationServiceToUserEntity(any()))
                .thenReturn(testAdminEntity);

        //Act
        userServiceTest.register(testUserRegModel);

        //Assert
        verify(passwordEncoderMock, times(1))
                .encode(testUserRegModel.getPassword());
        verify(userRepositoryMock, times(1))
                .save(testAdminEntity);

    }

    @Test
    public void testRegister_User() {
        UserRegistrationServiceModel testUserRegModel = UserRegistrationServiceModel.builder()
                .firstName("User")
                .lastName("Userov")
                .username("user")
                .email("user@email.com")
                .password("12345")
                .build();

        when(userRepositoryMock.count())
                .thenReturn(1L);

        when(userRoleRepositoryMock.findByRole(UserRoleEnum.USER))
                .thenReturn(testUserRole);

        when(userMapperMock.userRegistrationServiceToUserEntity(any()))
                .thenReturn(testUserEntity);

        //ACT
        userServiceTest.register(testUserRegModel);

        //Assert
        verify(passwordEncoderMock, times(1))
                .encode(testUserRegModel.getPassword());
        verify(userRepositoryMock, times(1))
                .save(testUserEntity);

    }


    //getUserProfileByUsername
    @Test
    public void testGetUserProfileByUsername() {
        UserProfileViewModel userProfileViewModelTest = UserProfileViewModel.builder()
                .firstName(testUserEntity.getFirstName())
                .lastName(testUserEntity.getLastName())
                .username(testUserEntity.getUsername())
                .email(testUserEntity.getEmail())
                .profilePictureUrl(testUserEntity.getProfilePictureUrl())
                .bookings(new ArrayList<>())
                .build();

        when(userRepositoryMock.findByUsername(testUserEntity.getUsername()))
                .thenReturn(Optional.of(testUserEntity));

        when(userMapperMock.userEntityToProfileViewModel(testUserEntity))
                .thenReturn(userProfileViewModelTest);


        UserProfileViewModel userProfile =
                userServiceTest.getUserProfileByUsername(testUserEntity.getUsername());


        assertNotNull(userProfile);
        assertEquals(testUserEntity.getUsername(), userProfile.getUsername());

    }

    @Test
    public void testGetUserProfileByUsername_ThrowsWhenUserNotFound() {
        assertThrows(UserNotFoundException.class,
                () -> userServiceTest.getUserProfileByUsername("nonExistent"));
    }


    //changeProfilePic
    @Test
    public void testChangeProfilePic() throws IOException {
        UserPictureServiceModel userPictureModelTest = UserPictureServiceModel.builder()
                .username(testUserEntity.getUsername())
                .filePicture(new MockMultipartFile("filePicture",
                        "testPic.jpg",
                        "text/plain",
                        "testPic.jpg".getBytes()))
                .build();

        when(userRepositoryMock.findByUsername(any(String.class)))
                .thenReturn(Optional.of(testUserEntity));


        userServiceTest.changeProfilePic(userPictureModelTest);


        verify(userRepositoryMock, times(1)).save(testUserEntity);

    }

    @Test
    public void testChangeProfilePic_throwsWhenUserNotFound() {
        assertThrows(UserNotFoundException.class,
                () -> userServiceTest.changeProfilePic(new UserPictureServiceModel()));
    }


    //userExists
    @Test
    public void testUserExists_returnsTrueIfExists() {
        when(userRepositoryMock.findByUsername(any()))
                .thenReturn(Optional.of(new UserEntity()));

        assertTrue(userServiceTest.userExists("user"));
    }

    @Test
    public void testUserExists_returnsFalseIfNotExists() {
        when(userRepositoryMock.findByUsername(any()))
                .thenReturn(Optional.empty());
        assertFalse(userServiceTest.userExists("user"));
    }


    //getAllUsers
    @Test
    public void testGetAllUsers() {
        List<UserEntity> userEntities =
                Arrays.asList(new UserEntity(), new UserEntity());

        when(userRepositoryMock.findAll())
                .thenReturn(userEntities);

        when(userMapperMock.mapUserEntityToViewModel(any()))
                .thenReturn(new UserViewModel());

        List<UserViewModel> result = userServiceTest.getAllUsers();

        assertNotNull(result);
        assertEquals(userEntities.size(), result.size());
    }


    //getUserById
    @Test
    public void testGetUserById() {
        UserEntity userEntity = new UserEntity();
        when(userRepositoryMock.findById(userIdFake))
                .thenReturn(Optional.of(userEntity));

        when(userMapperMock.mapUserEntityToViewModel(userEntity))
                .thenReturn(new UserViewModel());

        UserViewModel result = userServiceTest.getUserById(userIdFake);

        assertNotNull(result);
    }

    @Test
    public void testGetUserById_throwsWhenUserNotFound() {
        assertThrows(UserNotFoundException.class,
                () -> userServiceTest.getUserById(userIdFake));
    }


    //updateUserRoles
    @Test
    public void testUpdateUserRoles() {
        Set<UserRoleEntity> newRoles = Set.of(testUserRole);

        when(userRepositoryMock.findById(userId))
                .thenReturn(Optional.of(testAdminEntity));


        userServiceTest.updateUserRoles(userId, newRoles);


        verify(userRepositoryMock, times(1))
                .save(testAdminEntity);
    }

    @Test
    public void testUpdateUserRoles_throwsWhenUserNotFound() {
        assertThrows(UserNotFoundException.class,
                () -> userServiceTest.updateUserRoles(1L, new HashSet<>()));
    }

    @Test
    public void testUpdateUserRoles_doesNotUpdateRolesWhenNull() {
        when(userRepositoryMock.findById(userId))
                .thenReturn(Optional.of(testUserEntity));


        userServiceTest.updateUserRoles(userId, null);


        verify(userRepositoryMock, times(0))
                .save(testUserEntity);
    }


    //deleteUserById
    @Test
    public void testDeleteUserById() {
        BookingEntity testBooking1 = new BookingEntity();
        testBooking1.setId(1L);

        when(userRepositoryMock.findById(userId))
                .thenReturn(Optional.of(testUserEntity));

        when(bookingServiceMock.getActiveBookingsByUser(testUserEntity))
                .thenReturn(List.of(testBooking1));


        userServiceTest.deleteUserById(userId);


        verify(bookingServiceMock, times(1))
                .cancelBooking(anyLong());

        verify(userRepositoryMock, times(1))
                .delete(testUserEntity);
    }


    //editUserData
    @Test
    void testEditUserData() {
        UserEditServiceModel editServiceModel = new UserEditServiceModel();

        when(userRepositoryMock.findById(userId))
                .thenReturn(Optional.of(testUserEntity));


        userServiceTest.editUserData(userId, editServiceModel);


        verify(userRepositoryMock, times(1))
                .save(testUserEntity);
    }


    //isLoggedUserTheAccountHolder
    @Test
    public void testIsLoggedUserTheAccountHolder_returnsTrueIfItIsHolder() {
        UserEntity editedUserEntity = new UserEntity();
        editedUserEntity.setId(userId);

        when(userRepositoryMock.findByUsername(testUserEntity.getUsername()))
                .thenReturn(Optional.of(testUserEntity));

        when(userRepositoryMock.findById(userId))
                .thenReturn(Optional.of(editedUserEntity));


        boolean result =
                userServiceTest.isLoggedUserTheAccountHolder(testUserEntity.getUsername(), userId);


        assertTrue(result);
    }

    @Test
    public void testIsLoggedUserTheAccountHolder_returnsFalseIfItIsNotHolder() {
        UserEntity editedUserEntity = new UserEntity();
        editedUserEntity.setId(userIdFake);

        when(userRepositoryMock.findByUsername(testUserEntity.getUsername()))
                .thenReturn(Optional.of(testUserEntity));

        when(userRepositoryMock.findById(userIdFake))
                .thenReturn(Optional.of(editedUserEntity));


        boolean result =
                userServiceTest.isLoggedUserTheAccountHolder(testUserEntity.getUsername(), userIdFake);


        assertFalse(result);
    }

    @Test
    public void testIsLoggedUserTheAccountHolder_throwsWhenUserNotFound() {
        when(userRepositoryMock.findByUsername(testUserEntity.getUsername()))
                .thenReturn(Optional.of(testUserEntity));

        assertThrows(UserNotFoundException.class,
                () -> userServiceTest.isLoggedUserTheAccountHolder(testUserEntity.getUsername(), userIdFake));
    }


    //isNewEmailDifferentAndExisting
    @Test
    public void testIsNewEmailDifferentAndExisting() {
        String newEmail = "newemail@email.com";

        when(userRepositoryMock.findById(userId))
                .thenReturn(Optional.of(testUserEntity));

        when(userRepositoryMock.findByEmail(newEmail))
                .thenReturn(Optional.of(new UserEntity()));


        boolean result =
                userServiceTest.isNewEmailDifferentAndExisting(userId, newEmail);


        assertTrue(result);
    }

    @Test
    public void testIsNewEmailDifferentAndExisting_returnsFalseWhenEmailNotExisting() {
        String newEmail = "newemail@email.com";

        when(userRepositoryMock.findById(userId))
                .thenReturn(Optional.of(testUserEntity));

        when(userRepositoryMock.findByEmail(newEmail))
                .thenReturn(Optional.empty());


        boolean result =
                userServiceTest.isNewEmailDifferentAndExisting(userId, newEmail);


        assertFalse(result);
    }


    //isNewUsernameDifferentAndExisting
    @Test
    public void testIsNewUsernameDifferentAndExisting() {
        String newUsername = "newUsername";

        when(userRepositoryMock.findById(userId))
                .thenReturn(Optional.of(testUserEntity));

        when(userRepositoryMock.findByUsername(newUsername))
                .thenReturn(Optional.of(new UserEntity()));


        boolean result =
                userServiceTest.isNewUsernameDifferentAndExisting(userId, newUsername);


        assertTrue(result);
    }

    @Test
    public void testIsNewUsernameDifferentAndExisting_returnsFalseWhenUsernameNotExisting() {
        String newUsername = "newUsername";

        when(userRepositoryMock.findById(userId))
                .thenReturn(Optional.of(testUserEntity));

        when(userRepositoryMock.findByUsername(newUsername))
                .thenReturn(Optional.empty());


        boolean result = userServiceTest.isNewUsernameDifferentAndExisting(userId, newUsername);


        assertFalse(result);
    }

}
