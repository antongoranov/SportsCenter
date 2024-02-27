package com.sportscenter.web;

import com.sportscenter.init.TestDataInit;
import com.sportscenter.repository.UserRepository;
import com.sportscenter.service.impl.EmailService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserRegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataInit testData;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private EmailService emailServiceMock;

    @BeforeEach
    public void setUp() {
        testData.initRoles();
    }

    @AfterEach
    public void tearDown() {
        testData.clearDB();
    }

    @Test
    public void testRegister_returnsCorrectView() throws Exception {
        mockMvc.perform(get("/users/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("userRegistrationModel"));

    }

    @Test
    public void testRegisterUser_registersUserWithCorrectFields() throws Exception {
        mockMvc.perform(post("/users/register")
                        .with(csrf())
                        .param("firstName", "fn test")
                        .param("lastName", "ln test")
                        .param("email", "test@email.com")
                        .param("username", "testUsername")
                        .param("password", "testPassword")
                        .param("confirmPassword", "testPassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/login"));

        assertTrue(userRepository.findByUsername("testUsername").isPresent());
    }

    @Test
    public void testRegisterUser_registersUserWithIncorrectDataFails() throws Exception {
        mockMvc.perform(post("/users/register")
                        .with(csrf())
                        .param("firstName", "fn test")
                        .param("lastName", "ln test")
                        .param("email", "Incorrect EMAIL")
                        .param("username", "testUsername")
                        .param("password", "testPassword")
                        .param("confirmPassword", "testPassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/register"));

        assertTrue(userRepository.findByUsername("testUsername").isEmpty());
    }

    @Test
    public void testRegisterUser_registersUserWithDifferentPasswordsFails() throws Exception {
        mockMvc.perform(post("/users/register")
                        .with(csrf())
                        .param("firstName", "fn test")
                        .param("lastName", "ln test")
                        .param("email", "test@email.com")
                        .param("username", "testUsername")
                        .param("password", "testPassword")
                        .param("confirmPassword", "different"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/register"));

        assertTrue(userRepository.findByUsername("testUsername").isEmpty());
    }


}
