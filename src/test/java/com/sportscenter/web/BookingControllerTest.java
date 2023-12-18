package com.sportscenter.web;

import com.sportscenter.init.TestDataInit;
import com.sportscenter.model.entity.SportClassEntity;
import com.sportscenter.model.entity.UserEntity;
import com.sportscenter.model.service.UserSearchServiceModel;
import com.sportscenter.model.view.SportClassBookingViewModel;
import com.sportscenter.model.view.SportClassViewModel;
import com.sportscenter.service.BookingService;
import com.sportscenter.service.SportClassService;
import com.sportscenter.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestDataInit testData;

    @MockBean
    private SportClassService sportClassServiceMock;

    @MockBean
    private BookingService bookingServiceMock;

    @MockBean
    private UserService userServiceMock;

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


    //getBookSportClass
    @Test
    @WithMockUser(username = "user")
    public void testGetBookSportClass_returnsCorrectView() throws Exception {

        SportClassBookingViewModel testView = SportClassBookingViewModel.builder()
                .sportId(1L)
                .sportDescription("test sport dec")
                .instructorName("Ivan Stoqnov")
                .maxCapacity(20)
                .currentCapacity(0)
                .dayOfWeek(DayOfWeek.MONDAY)
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(11, 0))
                .build();

        when(sportClassServiceMock.getSportClassById(1L))
                .thenReturn(testView);
        when(sportClassServiceMock.hasAvailableSpots(1L))
                .thenReturn(true);

        mockMvc.perform(get("/bookSportClass/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("sportClass"))
                .andExpect(model().attributeDoesNotExist("noAvailableSpots"))
                .andExpect(view().name("sport-class-book"));
    }

    @Test
    @WithAnonymousUser
    public void testGetBookSportClass_redirectsWhenAnonymous() throws Exception {
        mockMvc.perform(get("/bookSportClass/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }

    @Test
    @WithMockUser(username = "user")
    public void testGetBookSportClass_containsNoAvailableSpotsAttribute() throws Exception {

        SportClassBookingViewModel testView = SportClassBookingViewModel.builder()
                .sportId(1L)
                .sportDescription("test sport dec")
                .instructorName("Ivan Stoqnov")
                .maxCapacity(20)
                .currentCapacity(0)
                .dayOfWeek(DayOfWeek.MONDAY)
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(11, 0))
                .build();

        when(sportClassServiceMock.getSportClassById(1L))
                .thenReturn(testView);
        when(sportClassServiceMock.hasAvailableSpots(1L))
                .thenReturn(false);

        mockMvc.perform(get("/bookSportClass/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("sportClass"))
                .andExpect(model().attributeExists("noAvailableSpots"))
                .andExpect(view().name("sport-class-book"));
    }


    //bookSportClass
    @Test
    @WithMockUser(username = "user")
    public void testBookSportClass_returnsCorrectView() throws Exception {

        when(bookingServiceMock.hasActiveBookings(any(UserDetails.class)))
                .thenReturn(false);

        mockMvc.perform(post("/bookSportClass/1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/myBookings"));
    }

    @Test
    @WithMockUser(username = "user")
    public void testBookSportClass_redirectsWhenHavingActiveBookings() throws Exception {

        when(bookingServiceMock.hasActiveBookings(any(UserDetails.class)))
                .thenReturn(true);

        mockMvc.perform(post("/bookSportClass/1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bookSportClass/1"));
    }


    //userBookings
    @Test
    @WithMockUser(username = "user")
    public void testUserBookings_returnsCorrectView() throws Exception {

        when(bookingServiceMock.findBookingsByUser(any(UserDetails.class)))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/myBookings"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("bookingsByUser"))
                .andExpect(view().name("user-bookings"));
    }

    @Test
    @WithAnonymousUser
    public void testUserBookings_redirectsWhenAnonymous() throws Exception {
        mockMvc.perform(get("/myBookings"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }


    //cancelBooking
    @Test
    @WithMockUser(username = "user")
    public void testCancelBooking_redirectsWhenSuccessful() throws Exception {

    //doNothing().when(bookingServiceMock).cancelBooking(1L);

        mockMvc.perform(patch("/myBookings/cancelBooking/1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/myBookings"));

        verify(bookingServiceMock, times(1))
                .cancelBooking(1L);
    }

    @Test
    @WithAnonymousUser
    public void testCancelBooking_redirectsWhenAnonymous() throws Exception {

        mockMvc.perform(patch("/myBookings/cancelBooking/1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));

        verify(bookingServiceMock, times(0))
                .cancelBooking(1L);
    }


    //ADMIN allBookings
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "USER"})
    public void testAllBookings_returnsCorrectViewWhenSuccessful() throws Exception {

        when(userServiceMock.userExists(anyString()))
                .thenReturn(true);
        when(bookingServiceMock.findBookingsByUsername(any()))
                .thenReturn(new ArrayList<>());

        mockMvc.perform(get("/bookings/all")
                        .param("username", "testUsername"))
                .andExpect(status().isOk())
                .andExpect(view().name("search-user-bookings"))
                .andExpect(model().attributeExists("userSearch"))
                .andExpect(model().attributeExists("allBookingsByUser"))
                .andExpect(model().attributeDoesNotExist("userNotExist"));

    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "USER"})
    public void testAllBookings_modelContainsUserNotExistAttr() throws Exception {

        when(userServiceMock.userExists(anyString()))
                .thenReturn(false);

        mockMvc.perform(get("/bookings/all")
                        .param("username", "testUsername"))
                .andExpect(status().isOk())
                .andExpect(view().name("search-user-bookings"))
                .andExpect(model().attributeExists("userSearch"))
                .andExpect(model().attributeExists("userNotExist"))
                .andExpect(model().attributeDoesNotExist("allBookingsByUser"));

    }

    @Test
    @WithMockUser(username = "user")
    public void testAllBookings_forbiddenWhenNotAdmin() throws Exception {

        mockMvc.perform(get("/bookings/all")
                        .param("username", "testUsername"))
                .andExpect(status().isForbidden());

    }


    //ADMIN cancelBookingAdmin
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "USER"})
    public void testCancelBookingAdmin_redirectsWhenSuccessful() throws Exception {

        mockMvc.perform(patch("/bookings/all/cancelBooking/1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bookings/all"));

        verify(bookingServiceMock, times(1))
                .cancelBooking(1L);
    }

    @Test
    @WithMockUser(username = "user")
    public void testCancelBookingAdmin_forbiddenWhenNotAdmin() throws Exception {

        mockMvc.perform(patch("/bookings/all/cancelBooking/1")
                        .with(csrf()))
                .andExpect(status().isForbidden());

        verify(bookingServiceMock, times(0))
                .cancelBooking(1L);
    }

    @Test
    @WithAnonymousUser
    public void testCancelBookingAdmin_redirectsWhenAnonymous() throws Exception {

        mockMvc.perform(patch("/bookings/all/cancelBooking/1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));

        verify(bookingServiceMock, times(0))
                .cancelBooking(1L);
    }


    //ADMIN acceptBooking
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "USER"})
    public void testAcceptBookingAdmin_redirectsWhenSuccessful() throws Exception {

        mockMvc.perform(patch("/bookings/all/acceptBooking/1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bookings/all"));

        verify(bookingServiceMock, times(1))
                .acceptBooking(1L);
    }

    @Test
    @WithMockUser(username = "user")
    public void testAcceptBookingAdmin_forbiddenWhenNotAdmin() throws Exception {

        mockMvc.perform(patch("/bookings/all/acceptBooking/1")
                        .with(csrf()))
                .andExpect(status().isForbidden());

        verify(bookingServiceMock, times(0))
                .acceptBooking(1L);
    }

    @Test
    @WithAnonymousUser
    public void testAcceptBookingAdmin_redirectsWhenAnonymous() throws Exception {

        mockMvc.perform(patch("/bookings/all/acceptBooking/1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));

        verify(bookingServiceMock, times(0))
                .acceptBooking(1L);
    }
}
