package com.sportscenter.web;

import com.sportscenter.init.TestDataInit;
import com.sportscenter.model.entity.UserEntity;
import com.sportscenter.repository.BookingRepository;
import com.sportscenter.repository.UserRepository;
import com.sportscenter.service.QrCodeService;
import com.sportscenter.service.impl.QrCodeServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class QrCodeControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestDataInit testData;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookingRepository bookingRepository;

    @BeforeEach
    public void setUp(){
        testData.initQrCode();
    }

    @AfterEach
    public void tearDown() {
        testData.clearDB();
    }

    //getQrCodeForBooking
    @Test
    @WithMockUser(username = "user")
    public void testGetQrCodeForBooking() throws Exception {
        //due to tearDown() the id on db level isi incremented by 1; this affects BookingRestControllerTest also
        Long bookingId = bookingRepository.findAll().get(0).getId();
        mockMvc.perform(get("/qrCodes/{bookingId}", bookingId))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("qrCodeForBooking"))
                .andExpect(view().name("qr-code-for-booking"));
    }

    @Test
    @WithMockUser(username = "invalidUsername")
    public void testGetQrCodeForBooking_notBookingOwner() throws Exception {
        Long bookingId = bookingRepository.findAll().get(0).getId();
        UserEntity invalidUserTest = UserEntity.builder()
                .username("invalidUsername")
                .firstName("")
                .lastName("")
                .password("")
                .email("")
                .build();
        userRepository.save(invalidUserTest);

        mockMvc.perform(get("/qrCodes/{bookingId}", bookingId))
                .andExpect(status().isForbidden());

    }
}
