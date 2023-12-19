package com.sportscenter.web;

import com.sportscenter.init.TestDataInit;
import com.sportscenter.model.entity.UserEntity;
import com.sportscenter.model.entity.UserRoleEntity;
import com.sportscenter.model.mapper.UserMapper;
import com.sportscenter.model.service.UserEditServiceModel;
import com.sportscenter.model.service.UserPictureServiceModel;
import com.sportscenter.model.view.UserProfileViewModel;
import com.sportscenter.model.view.UserViewModel;
import com.sportscenter.service.UserRoleService;
import com.sportscenter.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataInit testData;

    @MockBean
    private UserService userServiceMock;

    @MockBean
    private UserRoleService userRoleServiceMock;

    //todo
    @Autowired
    private UserMapper userMapper;

    private UserEntity testAdmin;
    private UserEntity testUser;

    @BeforeEach
    public void setUp() {
        testAdmin = testData.initAdmin();
        testUser = testData.initUser();
    }

    @AfterEach
    public void tearDown() {
        testData.clearDB();
    }

    //getMyProfile
    @Test
    @WithMockUser(username = "user")
    public void testGetMyProfile_returnsCorrectView() throws Exception {

        UserProfileViewModel testUserProfileView = new UserProfileViewModel();
        testUserProfileView.setBookings(new ArrayList<>());

        when(userServiceMock.getUserProfileByUsername("user"))
                .thenReturn(testUserProfileView);

        mockMvc.perform(get("/users/myProfile"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(view().name("profile"));
    }

    @Test
    @WithAnonymousUser
    public void testGetMyProfile_redirectsWhenAnonymous() throws Exception {

        mockMvc.perform(get("/users/myProfile"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }


    //changeProfilePic
    @Test
    @WithMockUser(username = "user")
    public void testChangeProfilePic_changesProfilePicWithCorrectData() throws Exception {

        MockMultipartFile mockFileTest =
                new MockMultipartFile("filePicture", //the name of the binding model field
                        "testPic.jpg",
                        "text/plain",
                        "testPic.jpg".getBytes());

        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/users/myProfile/uploadProfilePic");
        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PATCH");
                return request;
            }
        });

        mockMvc.perform(builder
                        .file(mockFileTest)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/myProfile"));

        verify(userServiceMock, times(1))
                .changeProfilePic(any(UserPictureServiceModel.class));

    }


    //getEditUser
    @Test
    @WithMockUser(username = "user")
    public void testGetEditUser_returnsCorrectView() throws Exception {
        when(userServiceMock.isLoggedUserTheAccountHolder("user", 1L))
                .thenReturn(true);

        when(userServiceMock.getUserById(1L))
                .thenReturn(new UserViewModel());

        mockMvc.perform(get("/users/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("user-edit"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    @WithMockUser(username = "user")
    public void testGetEditUser_isForbiddenWhenLoggedUserIsNotTheAccountHolder() throws Exception {
        when(userServiceMock.isLoggedUserTheAccountHolder("user", 2L))
                .thenReturn(false);

        mockMvc.perform(get("/users/edit/1"))
                .andExpect(status().isForbidden());
    }


    //TODO:
    //editUser
    @Test
    @WithMockUser(username = "user")
    public void testEditUser_returnsCorrectViewIfSuccessful() throws Exception {

        when(userServiceMock.isNewEmailDifferentAndExisting(1L, "newEmail@email.com"))
                .thenReturn(false);
        when(userServiceMock.isNewUsernameDifferentAndExisting(1L, "newUserName"))
                .thenReturn(false);

        mockMvc.perform(put("/users/edit/1")
                        .param("id", "1")
                        .param("firstName", "newName")
                        .param("lastName", "newLastName")
                        .param("email", "newEmail@email.com")
                        .param("username", "newUserName")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/login"));

        verify(userServiceMock, times(1))
                .editUserData(any(Long.class), any(UserEditServiceModel.class));
    }

    @Test
    @WithMockUser(username = "user")
    public void testEditUser_redirectsIfBindingResultHasErrors() throws Exception {
        mockMvc.perform(put("/users/edit/1")
                        .param("id", "1")
                        .param("firstName", "")
                        .param("lastName", "")
                        .param("email", "newEmail@")
                        .param("username", "")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/edit/1"));

        verify(userServiceMock, times(0))
                .editUserData(any(Long.class), any(UserEditServiceModel.class));
    }

    @Test
    @WithMockUser(username = "user")
    public void testEditUser_redirectsIfNewEmailDifferentAndExisting() throws Exception {

        when(userServiceMock.isNewEmailDifferentAndExisting(1L, "newEmail@email.com"))
                .thenReturn(true);
        when(userServiceMock.isNewUsernameDifferentAndExisting(1L, "newUserName"))
                .thenReturn(false);

        mockMvc.perform(put("/users/edit/1")
                        .param("id", "1")
                        .param("firstName", "newName")
                        .param("lastName", "newLastName")
                        .param("email", "newEmail@email.com")
                        .param("username", "newUserName")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/edit/1"))
                .andExpect(flash().attributeExists("newEmailOrUsernameExisting"));

        verify(userServiceMock, times(0))
                .editUserData(any(Long.class), any(UserEditServiceModel.class));
    }

    @Test
    @WithMockUser(username = "user")
    public void testEditUser_redirectsIfNewUsernameDifferentAndExisting() throws Exception {

        when(userServiceMock.isNewEmailDifferentAndExisting(1L, "newEmail@email.com"))
                .thenReturn(false);
        when(userServiceMock.isNewUsernameDifferentAndExisting(1L, "newUserName"))
                .thenReturn(true);

        mockMvc.perform(put("/users/edit/1")
                        .param("id", "1")
                        .param("firstName", "newName")
                        .param("lastName", "newLastName")
                        .param("email", "newEmail@email.com")
                        .param("username", "newUserName")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/edit/1"))
                .andExpect(flash().attributeExists("newEmailOrUsernameExisting"));

        verify(userServiceMock, times(0))
                .editUserData(any(Long.class), any(UserEditServiceModel.class));
    }



    //allUsers
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "USER"})
    public void testAllUsers_returnsCorrectViewWhenSuccessful() throws Exception {
        when(userServiceMock.getAllUsers())
                .thenReturn(new ArrayList<>());

        mockMvc.perform(get("/users/all"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("allUsers"))
                .andExpect(view().name("users-all"));
    }

    @Test
    @WithMockUser(username = "user")
    public void testAllUsers_isForbiddenWhenNotAdmin() throws Exception {
        mockMvc.perform(get("/users/all"))
                .andExpect(status().isForbidden());
    }



    //updateUserRoles
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "USER"})
    public void testUpdateUserRoles_returnsCorrectViewWhenSuccessful() throws Exception {

        mockMvc.perform(patch("/users/1/roles")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/all"));

        verify(userServiceMock, times(1))
                .updateUserRoles(any(Long.class), any());

    }

    @Test
    @WithMockUser(username = "user")
    public void testUpdateUserRoles_isForbiddenWhenNotAdmin() throws Exception {

        mockMvc.perform(patch("/users/1/roles")
                        .with(csrf()))
                .andExpect(status().isForbidden());

        verify(userServiceMock, times(0))
                .updateUserRoles(any(Long.class), any());

    }



    //deleteUser
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "USER"})
    public void testDeleteUser_returnsCorrectViewWhenSuccessful() throws Exception {

        mockMvc.perform(delete("/users/1/delete")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/all"));

        verify(userServiceMock, times(1))
                .deleteUserById(any(Long.class));

    }

    @Test
    @WithMockUser(username = "user")
    public void testDeleteUser_isForbiddenWhenNotAdmin() throws Exception {

        mockMvc.perform(delete("/users/1/delete")
                        .with(csrf()))
                .andExpect(status().isForbidden());

        verify(userServiceMock, times(0))
                .deleteUserById(any(Long.class));

    }
}
