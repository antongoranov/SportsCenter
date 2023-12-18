package com.sportscenter.web;


import com.sportscenter.init.TestDataInit;
import com.sportscenter.model.entity.*;
import com.sportscenter.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SportClassControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataInit testData;

    @Autowired
    private SportClassRepository sportClassRepository;

    private UserEntity testUser;

    @BeforeEach
    public void setUp() {
        testUser = testData.initAdmin();
    }

    @AfterEach
    public void tearDown() {
        testData.clearDB();
    }

    @Test
    @WithAnonymousUser
    public void testAllSportClassesReturnsCorrectView() throws Exception {

        mockMvc.perform(get("/sportClasses/all"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("sportClasses"))
                .andExpect(view().name("schedule"));

    }

    @Test
    @WithAnonymousUser
    public void testAddSportClassAccessedByAnonymousIsForbidden() throws Exception {

        mockMvc.perform(get("/sportClasses/add"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }

    @Test
    @WithMockUser(
            username = "admin",
            roles = {"ADMIN", "USER"})
    public void testAddSportClassReturnsCorrectView() throws Exception {
        mockMvc.perform(get("/sportClasses/add"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("sportClass"))
                .andExpect(model().attributeExists("sportClasses"))
                .andExpect(model().attributeExists("allInstructors"))
                .andExpect(view().name("sport-class-add"));

    }

    @Test
    @WithMockUser(
            username = "admin",
            roles = {"ADMIN", "USER"})
    public void testAddSportClassPostCreatesNewSportClass() throws Exception {

        //check info document
        testData.initInstructor();

        mockMvc.perform(post("/sportClasses/add")
                .param("instructorId", "1")
                .param("dayOfWeek", "MONDAY")
                .param("startTime", "10:00")
                .param("endTime", "11:00")
                .param("maxCapacity", "20")
                .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/sportClasses/all"));

        assertEquals(1, sportClassRepository.count());

    }

    @Test
    @WithMockUser(
            username = "admin",
            roles = {"ADMIN", "USER"})
    public void testAddSportClassPostDoesNotCreateClassWithInvalidData() throws Exception {

        testData.initInstructor();

        mockMvc.perform(post("/sportClasses/add")
                        .param("instructorId", "1")
                        .param("dayOfWeek", "1")
                        .param("startTime", "1")
                        .param("endTime", "1")
                        .param("maxCapacity", "1")
                        .with(csrf())
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/sportClasses/add"));

        assertEquals(0, sportClassRepository.count());
    }

    @Test
    @WithMockUser(
            username = "admin",
            roles = {"ADMIN", "USER"})
    public void testAddSportClassPostDoesNotCreateClassWhenTimeSpotIsTaken() throws Exception {

        testData.initSportClass();

        mockMvc.perform(post("/sportClasses/add")
                        .param("instructorId", "1")
                        .param("dayOfWeek", "MONDAY")
                        .param("startTime", "10:00")
                        .param("endTime", "11:00")
                        .param("maxCapacity", "20")
                        .with(csrf())
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/sportClasses/add"));

        assertEquals(1, sportClassRepository.count());
    }


}
