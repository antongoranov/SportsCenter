package com.sportscenter.service;

import com.sportscenter.exception.UserNotFoundException;
import com.sportscenter.model.entity.*;
import com.sportscenter.model.enums.BookingStatusEnum;
import com.sportscenter.model.mapper.BookingMapper;
import com.sportscenter.model.service.UserSearchServiceModel;
import com.sportscenter.model.view.BookingViewModel;
import com.sportscenter.repository.BookingRepository;
import com.sportscenter.repository.SportClassRepository;
import com.sportscenter.repository.UserRepository;
import com.sportscenter.service.impl.BookingServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension .class)
public class BookingServiceImplTest {

    @Mock
    private UserDetails userDetailsMock;
    @Mock
    private UserRepository userRepositoryMock;
    @Mock
    private BookingRepository bookingRepositoryMock;
    @Mock
    private SportClassRepository sportClassRepositoryMock;

    @Mock
    private SportClassService sportClassServiceMock;
    @Mock
    private QrCodeService qrCodeServiceMock;
    @Mock
    private BookingMapper bookingMapperMock;

    @Mock
    private UserEntity testUserEntity;

    @InjectMocks
    private BookingServiceImpl bookingServiceImplTest;

    //bookASportClass
    @Test
    public void testBookASportClass_booksSuccessfully(){
        Long sportClassId = 1L;
        SportClassEntity testSportClassEntity = mock(SportClassEntity.class);

        when(userRepositoryMock.findByUsername(userDetailsMock.getUsername()))
                .thenReturn(Optional.of(testUserEntity));
        when(sportClassRepositoryMock.findById(1L))
                .thenReturn(Optional.of(testSportClassEntity));

        bookingServiceImplTest.bookASportClass(userDetailsMock, sportClassId);

        verify(sportClassServiceMock, times(1))
                .updateCapacity(testSportClassEntity);

        verify(bookingRepositoryMock, times(1))
                .save(any(BookingEntity.class));
        verify(qrCodeServiceMock, times(1))
                .generateQrForBooking(any(BookingEntity.class));
    }

    @Test
    public void testBookASportClass_fails(){
        Long sportClassId = 1L;
        when(userRepositoryMock.findByUsername(userDetailsMock.getUsername()))
                .thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> bookingServiceImplTest.bookASportClass(userDetailsMock, sportClassId));

        verify(sportClassServiceMock, never())
                .updateCapacity(any(SportClassEntity.class));
        verify(bookingRepositoryMock, never())
                .save(any(BookingEntity.class));
        verify(qrCodeServiceMock, never())
                .generateQrForBooking(any(BookingEntity.class));

    }

    //hasActiveBookings
    @Test
    public void testHasActiveBookings_true(){
        when(userRepositoryMock.findByUsername(userDetailsMock.getUsername()))
                .thenReturn(Optional.of(testUserEntity));

        when(bookingRepositoryMock.findAllByUserAndStatus(testUserEntity, BookingStatusEnum.ACTIVE))
                .thenReturn(List.of(new BookingEntity()));

        boolean actual = bookingServiceImplTest.hasActiveBookings(userDetailsMock);

        assertTrue(actual);
    }

    @Test
    public void testHasActiveBookings_false(){
        when(userRepositoryMock.findByUsername(userDetailsMock.getUsername()))
                .thenReturn(Optional.of(testUserEntity));

        when(bookingRepositoryMock.findAllByUserAndStatus(testUserEntity, BookingStatusEnum.ACTIVE))
                .thenReturn(new ArrayList<>());

        boolean actual = bookingServiceImplTest.hasActiveBookings(userDetailsMock);

        assertFalse(actual);
    }

    @Test
    public void testHasActiveBookings_throws(){
        when(userRepositoryMock.findByUsername(userDetailsMock.getUsername()))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> bookingServiceImplTest.hasActiveBookings(userDetailsMock));

        verify(bookingRepositoryMock, never())
                .findAllByUserAndStatus(any(UserEntity.class), any(BookingStatusEnum.class));
    }

    //findBookingsByUser Integration tested

    //cancelBooking
    @Test
    public void testCancelBooking_successfully(){
        BookingEntity testBookingEntity = BookingEntity.builder()
                        .sportClass(new SportClassEntity())
                        .build();

        Long bookingId = 1L;

        when(bookingRepositoryMock.findById(bookingId))
                .thenReturn(Optional.of(testBookingEntity));

        bookingServiceImplTest.cancelBooking(bookingId);


        assertEquals(BookingStatusEnum.CANCELLED, testBookingEntity.getStatus());
        verify(sportClassServiceMock, times(1)).decreaseCapacity(any(SportClassEntity.class));
        verify(bookingRepositoryMock, times(1)).save(any(BookingEntity.class));
        verify(qrCodeServiceMock, times(1)).deleteQrCodeForBooking(bookingId);
    }

    //findBookingsByUsername
    @Test
    public void testFindBookingsByUsername_returnsCorrectListOfBookings(){
        String testUsername = "testUsername";
        UserSearchServiceModel userSearchServiceModelTest = new UserSearchServiceModel(testUsername);

        when(userRepositoryMock.findByUsername(testUsername))
                .thenReturn(Optional.of(testUserEntity));
        when(bookingRepositoryMock.findAllByUser(testUserEntity))
                .thenReturn(List.of(new BookingEntity()));
        when(bookingMapperMock.bookingEntityToViewModel(any(BookingEntity.class)))
                .thenReturn(new BookingViewModel());

        List<BookingViewModel> actual =
                bookingServiceImplTest.findBookingsByUsername(userSearchServiceModelTest);

        assertEquals(1, actual.size());
    }

    @Test
    public void testFindBookingsByUsername_throws(){
        String testUsername = "testUsername";
        UserSearchServiceModel userSearchServiceModelTest = new UserSearchServiceModel(testUsername);

        when(userRepositoryMock.findByUsername(testUsername))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> bookingServiceImplTest.findBookingsByUsername(userSearchServiceModelTest));

        verifyNoInteractions(bookingRepositoryMock, bookingMapperMock);
    }

    //acceptBooking
    @Test
    public void testAcceptBooking_accepts(){
        Long bookingId = 1L;
        BookingEntity testBookingEntity = BookingEntity.builder()
                .sportClass(new SportClassEntity())
                .build();

        when(bookingRepositoryMock.findById(bookingId))
                .thenReturn(Optional.of(testBookingEntity));

        bookingServiceImplTest.acceptBooking(bookingId);

        assertEquals(BookingStatusEnum.ACCEPTED, testBookingEntity.getStatus());
        verify(bookingRepositoryMock, times(1)).save(testBookingEntity);
    }

    //getActiveBookingsByUser
    @Test
    public void testGetActiveBookingsByUser_returnsCorrectListOfBookings(){
        BookingEntity testBookingEntity = new BookingEntity();
        testBookingEntity.setId(1L);

        when(bookingRepositoryMock.findAllByUserAndStatus(testUserEntity, BookingStatusEnum.ACTIVE))
                .thenReturn(List.of(testBookingEntity));

        List<BookingEntity> actual = bookingServiceImplTest.getActiveBookingsByUser(testUserEntity);

        assertEquals(1, actual.size());
    }

    //expireBookingsAtEndOfDay
    @Test
    public void testExpireBookingsAtEndOfDay_setsCorrectStatus(){
        DayOfWeek today = LocalDateTime.now().getDayOfWeek();
        List<BookingEntity> bookingEntitiesTestList = List.of(new BookingEntity());

        when(bookingRepositoryMock.findBySportClassDayOfWeekAndStatus(today, BookingStatusEnum.ACTIVE))
                .thenReturn(bookingEntitiesTestList);

        bookingServiceImplTest.expireBookingsAtEndOfDay();

        assertEquals(BookingStatusEnum.EXPIRED, bookingEntitiesTestList.get(0).getStatus());
        verify(bookingRepositoryMock, times(1))
                .save(bookingEntitiesTestList.get(0));
        verify(qrCodeServiceMock, times(1))
                .deleteQrCodeForBooking(bookingEntitiesTestList.get(0).getId());

    }

    @Test
    public void testExpireBookingsAtEndOfDay_noActiveBookings(){
        DayOfWeek today = LocalDateTime.now().getDayOfWeek();

        when(bookingRepositoryMock.findBySportClassDayOfWeekAndStatus(today, BookingStatusEnum.ACTIVE))
                .thenReturn(Collections.emptyList());

        bookingServiceImplTest.expireBookingsAtEndOfDay();

        verifyNoMoreInteractions(bookingRepositoryMock);
        verifyNoInteractions(qrCodeServiceMock);
    }

    //deleteExpiredBookingsAtEndOfWeek
    @Test
    public void testDeleteExpiredBookingsAtEndOfWeek_deletesBookings(){
        BookingEntity testBookingEntity = new BookingEntity();
        testBookingEntity.setId(1L);
        List<BookingEntity> bookingEntitiesTestList = List.of(testBookingEntity);

        when(bookingRepositoryMock.findByStatusNot(BookingStatusEnum.ACTIVE))
                .thenReturn(bookingEntitiesTestList);

        bookingServiceImplTest.deleteExpiredBookingsAtEndOfWeek();

        verify(qrCodeServiceMock, times(1))
                .deleteQrCodeForBooking(testBookingEntity.getId());
        verify(bookingRepositoryMock, times(1))
                .deleteAll(bookingEntitiesTestList);

    }

    @Test
    public void testDeleteExpiredBookingsAtEndOfWeek_doesNotDeleteBookings(){
        when(bookingRepositoryMock.findByStatusNot(BookingStatusEnum.ACTIVE))
                .thenReturn(Collections.emptyList());

        bookingServiceImplTest.deleteExpiredBookingsAtEndOfWeek();

        verifyNoInteractions(qrCodeServiceMock);
    }

}
