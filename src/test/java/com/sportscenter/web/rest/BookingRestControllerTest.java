package com.sportscenter.web.rest;

import com.sportscenter.init.TestDataInit;
import com.sportscenter.model.entity.BookingEntity;
import com.sportscenter.model.entity.UserEntity;
import com.sportscenter.repository.BookingRepository;
import com.sportscenter.service.BookingService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@SpringBootTest
@AutoConfigureMockMvc
public class BookingRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataInit testData;

    @Autowired
    private BookingRepository bookingRepository;

    private UserEntity testUser;

    @BeforeEach
    public void setUp() {
        testUser = testData.initUser();
    }

    @AfterEach
    public void tearDown() {
        testData.clearDB();
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testUserBookings_returnsCorrectData() throws Exception {
        testData.initBooking();
        Long bookingId = bookingRepository.findAll().get(0).getId();
        Long sportClassId = bookingRepository.findAll().get(0).getSportClass().getId();
        Long sportId = bookingRepository.findAll().get(0).getSportClass().getSport().getId();

        mockMvc.perform(get("/api/myBookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].id", is(bookingId.intValue())))
                    .andExpect(jsonPath("$.[0].sportClass").isNotEmpty())
                    .andExpect(jsonPath("$.[0].sportClass.id", is(sportClassId.intValue())))
                    .andExpect(jsonPath("$.[0].sportClass.sportId", is(sportId.intValue())))
                    .andExpect(jsonPath("$.[0].sportClass.sportImageUrl", is("sport.com/image.jpg")))
                    .andExpect(jsonPath("$.[0].sportClass.sportDescription", is("test sport dec")))
                    .andExpect(jsonPath("$.[0].sportClass.instructorName", is("Ivan Stoqnov")))
                    .andExpect(jsonPath("$.[0].sportClass.maxCapacity", is(20)))
                    .andExpect(jsonPath("$.[0].sportClass.currentCapacity", is(0)))
                    .andExpect(jsonPath("$.[0].sportClass.dayOfWeek", is("MONDAY")))
                    .andExpect(jsonPath("$.[0].sportClass.startTime", is("10:00:00")))
                    .andExpect(jsonPath("$.[0].sportClass.endTime", is("11:00:00")))
                .andExpect(jsonPath("$.[0].status", is("ACTIVE")))
                .andExpect(jsonPath("$.[0].statusNotActive", is(false)));

    }

    @Test
    @WithAnonymousUser
    public void testUserBookings_withAnonymousRedirects() throws Exception {

        mockMvc.perform(get("/api/myBookings"))
                .andExpect(status().is3xxRedirection());

    }
}
