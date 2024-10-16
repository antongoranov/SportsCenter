package com.sportscenter.service;

import com.sportscenter.exception.QrCodeException;
import com.sportscenter.model.entity.*;
import com.sportscenter.model.enums.BookingStatusEnum;
import com.sportscenter.repository.BookingRepository;
import com.sportscenter.repository.QrCodeRepository;
import com.sportscenter.service.impl.QrCodeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;


import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QrCodeServiceImplTest {

    @Mock
    private QrCodeRepository qrCodeRepositoryMock;
    @Mock
    private BookingRepository bookingRepositoryMock;

    @InjectMocks
    private QrCodeServiceImpl qrCodeServiceTest;

    private BookingEntity bookingEntityTest;

    @BeforeEach
    public void setUp(){
        UserEntity userTest = UserEntity.builder()
                .username("test")
                .build();

        SportClassEntity sportClassTest = SportClassEntity.builder()
                .sport(SportEntity.builder()
                        .name("Test Sport")
                        .build())
                .instructor(InstructorEntity.builder()
                        .firstName("Instructor")
                        .lastName("Test")
                        .build())
                .dayOfWeek(DayOfWeek.MONDAY)
                .startTime(LocalTime.of(13, 0))
                .endTime(LocalTime.of(14, 0))
                .build();

        bookingEntityTest = BookingEntity.builder()
                .user(userTest)
                .sportClass(sportClassTest)
                .status(BookingStatusEnum.ACTIVE)
                .build();
    }

    //generateQrForBooking
    @Test
    public void testGenerateQrForBooking_generatesQr(){
        qrCodeServiceTest.generateQrForBooking(bookingEntityTest);

        verify(qrCodeRepositoryMock, times(1))
                .save(any(QrCodeEntity.class));
    }

//    @Test //to run need to set createQrCodeImage(BookingEntity booking) as public
//    public void testGenerateQrForBooking_throws() throws Exception {
//        QrCodeServiceImpl qrCodeServiceSpy = spy(qrCodeServiceTest);
//
//        doThrow(new IOException())
//                .when(qrCodeServiceSpy).createQrCodeImage(bookingEntityTest);
//
//        assertThrows(QrCodeException.class,
//                () -> qrCodeServiceSpy.generateQrForBooking(bookingEntityTest));
//
//        verify(qrCodeRepositoryMock, never()).save(any(QrCodeEntity.class));
//
//    }

    //deleteQrCodeForBooking
    @Test
    public void testDeleteQrCodeForBooking_deletesQrCode(){
        when(bookingRepositoryMock.findById(any(Long.class)))
                .thenReturn(Optional.of(bookingEntityTest));

        qrCodeServiceTest.deleteQrCodeForBooking(1L);

        verify(qrCodeRepositoryMock, times(1))
                .deleteByBooking(bookingEntityTest);
    }
}
