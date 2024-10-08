package com.sportscenter.service;

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
import com.sportscenter.service.impl.EmailService;
import com.sportscenter.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.time.LocalDateTime;
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
    @Mock
    private CloudinaryService cloudinaryServiceMock;
    @Mock
    private EmailService emailServiceMock;
    @Mock
    private PasswordResetTokenRepository pwResetTokenRepositoryMock;
    @Mock
    private UserDetailsService userDetailsServiceMock;
    @Mock
    private UserDetails userDetailsMock;

    @InjectMocks
    private UserServiceImpl userServiceTest;

    private UserRoleEntity testAdminRole;
    private UserRoleEntity testUserRole;

    private UserEntity testAdminEntity;
    private UserEntity testUserEntity;
    private static final Long USER_ID = 1L;
    private static final Long ADMIN_ID = 2L;
    private static final Long USER_ID_FAKE = 3L;

    @BeforeEach
    public void setUp() {
        testAdminRole = new UserRoleEntity(UserRoleEnum.ADMIN);
        testUserRole = new UserRoleEntity(UserRoleEnum.USER);

        testAdminEntity = UserEntity.builder()
                .firstName("Admin")
                .lastName("Adminov")
                .username("admin")
                .email("admin@email.com")
                .password("123456")
                .profilePictureUrl("url/testAdmin")
                .roles(Set.of(testAdminRole, testUserRole))
                .build();
        testAdminEntity.setId(ADMIN_ID);

        testUserEntity = UserEntity.builder()
                .firstName("User")
                .lastName("Userov")
                .username("user")
                .email("user@email.com")
                .password("12345")
                .profilePictureUrl("https://res.cloudinary.com/drksjtn0q/image/upload/v1708722519/sportscenter/users/testUser.jpg")
                .roles(Set.of(testUserRole))
                .build();
        testUserEntity.setId(USER_ID);
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

    //userWithEmailExists
    @Test
    public void testUserWithEmailExists_returnsTrueIfExists(){
        when(userRepositoryMock.findByEmail("user@email.com"))
                .thenReturn(Optional.of(testUserEntity));

        assertTrue(userServiceTest.userWithEmailExists("user@email.com"));
    }

    @Test
    public void testUserWithEmailExists_returnsFalseIfNotExists(){
        when(userRepositoryMock.findByEmail("test@email.com"))
                .thenReturn(Optional.empty());

        assertFalse(userServiceTest.userWithEmailExists("test@email.com"));
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
        when(userRepositoryMock.findById(USER_ID_FAKE))
                .thenReturn(Optional.of(userEntity));

        when(userMapperMock.mapUserEntityToViewModel(userEntity))
                .thenReturn(new UserViewModel());

        UserViewModel result = userServiceTest.getUserById(USER_ID_FAKE);

        assertNotNull(result);
    }

    @Test
    public void testGetUserById_throwsWhenUserNotFound() {
        assertThrows(UserNotFoundException.class,
                () -> userServiceTest.getUserById(USER_ID_FAKE));
    }

    //getUserByEmail
    @Test
    public void testGetUserByEmail(){
        when(userRepositoryMock.findByEmail("test@email.com"))
                .thenReturn(Optional.of(testUserEntity));

        UserEntity result = userServiceTest.getUserByEmail("test@email.com");

        assertEquals(result, testUserEntity);
    }

    @Test
    public void testGetUserByEmail_throwsWhenNotFound(){
        assertThrows(UserNotFoundException.class,
                () -> userServiceTest.getUserByEmail("fake@email.com"));
    }

    //updateUserRoles
    @Test
    public void testUpdateUserRoles() {
        Set<UserRoleEntity> newRoles = Set.of(testUserRole);

        when(userRepositoryMock.findById(USER_ID))
                .thenReturn(Optional.of(testAdminEntity));


        userServiceTest.updateUserRoles(USER_ID, newRoles);


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
        when(userRepositoryMock.findById(USER_ID))
                .thenReturn(Optional.of(testUserEntity));


        userServiceTest.updateUserRoles(USER_ID, null);


        verify(userRepositoryMock, times(0))
                .save(testUserEntity);
    }


    //deleteUserById
    @Test
    public void testDeleteUserById() {
        BookingEntity testBooking1 = new BookingEntity();
        testBooking1.setId(1L);

        when(userRepositoryMock.findById(USER_ID))
                .thenReturn(Optional.of(testUserEntity));

        when(bookingServiceMock.getActiveBookingsByUser(testUserEntity))
                .thenReturn(List.of(testBooking1));


        userServiceTest.deleteUserById(USER_ID);


        verify(bookingServiceMock, times(1))
                .cancelBooking(anyLong());

        verify(userRepositoryMock, times(1))
                .delete(testUserEntity);
    }


    //editUserData
    @Test
    void testEditUserData() {
        UserEditServiceModel editServiceModel = new UserEditServiceModel();

        when(userRepositoryMock.findById(USER_ID))
                .thenReturn(Optional.of(testUserEntity));


        userServiceTest.editUserData(USER_ID, editServiceModel);


        verify(userRepositoryMock, times(1))
                .save(testUserEntity);
    }


    //isLoggedUserTheAccountHolder
    @Test
    public void testIsLoggedUserTheAccountHolder_returnsTrueIfItIsHolder() {
        UserEntity editedUserEntity = new UserEntity();
        editedUserEntity.setId(USER_ID);

        when(userRepositoryMock.findByUsername(testUserEntity.getUsername()))
                .thenReturn(Optional.of(testUserEntity));

        when(userRepositoryMock.findById(USER_ID))
                .thenReturn(Optional.of(editedUserEntity));


        boolean result =
                userServiceTest.isLoggedUserTheAccountHolder(testUserEntity.getUsername(), USER_ID);


        assertTrue(result);
    }

    @Test
    public void testIsLoggedUserTheAccountHolder_returnsFalseIfItIsNotHolder() {
        UserEntity editedUserEntity = new UserEntity();
        editedUserEntity.setId(USER_ID_FAKE);

        when(userRepositoryMock.findByUsername(testUserEntity.getUsername()))
                .thenReturn(Optional.of(testUserEntity));

        when(userRepositoryMock.findById(USER_ID_FAKE))
                .thenReturn(Optional.of(editedUserEntity));


        boolean result =
                userServiceTest.isLoggedUserTheAccountHolder(testUserEntity.getUsername(), USER_ID_FAKE);


        assertFalse(result);
    }

    @Test
    public void testIsLoggedUserTheAccountHolder_throwsWhenUserNotFound() {
        when(userRepositoryMock.findByUsername(testUserEntity.getUsername()))
                .thenReturn(Optional.of(testUserEntity));

        assertThrows(UserNotFoundException.class,
                () -> userServiceTest.isLoggedUserTheAccountHolder(testUserEntity.getUsername(), USER_ID_FAKE));
    }


    //isNewEmailDifferentAndExisting
    @Test
    public void testIsNewEmailDifferentAndExisting() {
        String newEmail = "newemail@email.com";

        when(userRepositoryMock.findById(USER_ID))
                .thenReturn(Optional.of(testUserEntity));

        when(userRepositoryMock.findByEmail(newEmail))
                .thenReturn(Optional.of(new UserEntity()));


        boolean result =
                userServiceTest.isNewEmailDifferentAndExisting(USER_ID, newEmail);


        assertTrue(result);
    }

    @Test
    public void testIsNewEmailDifferentAndExisting_returnsFalseWhenEmailNotExisting() {
        String newEmail = "newemail@email.com";

        when(userRepositoryMock.findById(USER_ID))
                .thenReturn(Optional.of(testUserEntity));

        when(userRepositoryMock.findByEmail(newEmail))
                .thenReturn(Optional.empty());


        boolean result =
                userServiceTest.isNewEmailDifferentAndExisting(USER_ID, newEmail);


        assertFalse(result);
    }


    //isNewUsernameDifferentAndExisting
    @Test
    public void testIsNewUsernameDifferentAndExisting() {
        String newUsername = "newUsername";

        when(userRepositoryMock.findById(USER_ID))
                .thenReturn(Optional.of(testUserEntity));

        when(userRepositoryMock.findByUsername(newUsername))
                .thenReturn(Optional.of(new UserEntity()));


        boolean result =
                userServiceTest.isNewUsernameDifferentAndExisting(USER_ID, newUsername);


        assertTrue(result);
    }

    @Test
    public void testIsNewUsernameDifferentAndExisting_returnsFalseWhenUsernameNotExisting() {
        String newUsername = "newUsername";

        when(userRepositoryMock.findById(USER_ID))
                .thenReturn(Optional.of(testUserEntity));

        when(userRepositoryMock.findByUsername(newUsername))
                .thenReturn(Optional.empty());


        boolean result = userServiceTest.isNewUsernameDifferentAndExisting(USER_ID, newUsername);


        assertFalse(result);
    }

    //changeUserPassword
    @Test
    public void testChangeUserPassword_successfully(){
        PasswordChangeServiceModel pwdChangeServiceModelTest = new PasswordChangeServiceModel("testToken", "newPassword");
        PasswordResetToken pwdTest = new PasswordResetToken("testToken", testUserEntity, LocalDateTime.now().plusMinutes(1L));

        when(pwResetTokenRepositoryMock.findByToken("testToken"))
                .thenReturn(Optional.of(pwdTest));

        when(passwordEncoderMock.encode(pwdChangeServiceModelTest.getNewPassword()))
                .thenReturn("newPasswordEncoded");

        userServiceTest.changeUserPassword(pwdChangeServiceModelTest);

        assertEquals("newPasswordEncoded", testUserEntity.getPassword());
        verify(userRepositoryMock, times(1)).save(testUserEntity);

    }

    //createUserIfNotExist
    @Test
    public void testCreateUserIfNotExist(){
        String email = "new@example.com";

        when(userRepositoryMock.findByEmail(email))
                .thenReturn(Optional.empty());
        when(userMapperMock.userRegistrationServiceToUserEntity(any(UserRegistrationServiceModel.class)))
                .thenReturn(new UserEntity());
        when(userRoleRepositoryMock.findByRole(any(UserRoleEnum.class)))
                .thenReturn(new UserRoleEntity());
        when(passwordEncoderMock.encode(anyString()))
                .thenReturn("encodedPassword");

        userServiceTest.createUserIfNotExist(email);

        verify(userRepositoryMock, times(1))
                .findByEmail(email);
        verify(userRepositoryMock, times(1))
                .save(any(UserEntity.class));
    }

    @Test
    public void testCreateUserIfNotExist_userExists(){
        String existingEmail = testUserEntity.getEmail();

        when(userRepositoryMock.findByEmail(existingEmail))
                .thenReturn(Optional.of(testUserEntity));

        userServiceTest.createUserIfNotExist(testUserEntity.getEmail());

        verify(userRepositoryMock, times(1))
                .findByEmail(existingEmail);

        verify(userRepositoryMock, never())
                .save(any(UserEntity.class));

    }

    //login
    @Test
    public void testLogin_shouldAuthenticateUser(){
        String username = testUserEntity.getUsername();
        String password = testAdminEntity.getPassword();

        when(userDetailsServiceMock.loadUserByUsername(username))
                .thenReturn(userDetailsMock);
        when(userDetailsMock.getPassword()).thenReturn(password);
        when(userDetailsMock.getAuthorities()).thenReturn(new ArrayList<>());

        userServiceTest.login(username);
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        assertNotNull(authentication);
        assertTrue(authentication instanceof UsernamePasswordAuthenticationToken);
        assertEquals(userDetailsMock, authentication.getPrincipal());
        assertEquals(password, authentication.getCredentials());

    }
}
