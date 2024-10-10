package com.sportscenter.service;

import com.sportscenter.exception.ObjectNotFoundException;
import com.sportscenter.exception.UnableToProcessOperationException;
import com.sportscenter.model.entity.InstructorEntity;
import com.sportscenter.model.entity.SportClassEntity;
import com.sportscenter.model.entity.SportEntity;
import com.sportscenter.model.mapper.SportClassMapper;
import com.sportscenter.model.view.SportClassBookingViewModel;
import com.sportscenter.model.view.SportClassViewModel;
import com.sportscenter.repository.SportClassRepository;
import com.sportscenter.service.impl.SportClassServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SportClassServiceImplTest {

    @Mock
    private SportClassRepository sportClassRepositoryMock;
    @Mock
    private SportClassMapper sportClassMapperMock;

    @InjectMocks
    private SportClassServiceImpl sportClassServiceTest;

    //getMatchedSportClass
    @Test
    public void testGetMatchedSportClass_dayAndHourDontMatch(){
        SportClassViewModel sportClassViewModelTest =
                SportClassViewModel.builder()
                        .dayOfWeek(DayOfWeek.MONDAY)
                        .startTime(LocalTime.of(13, 0))
                        .build();

        List<SportClassViewModel> sportClassViewModelsTest = List.of(sportClassViewModelTest);

        SportClassViewModel matchedSportClass = sportClassServiceTest.getMatchedSportClass(
                sportClassViewModelsTest,
                2,
                14);

        assertNull(matchedSportClass);
    }

    @Test
    public void testGetMatchedSportClass_dayOrHourDontMatch(){
        SportClassViewModel sportClassViewModelTest =
                SportClassViewModel.builder()
                        .dayOfWeek(DayOfWeek.MONDAY)
                        .startTime(LocalTime.of(13, 0))
                        .build();

        List<SportClassViewModel> sportClassViewModelsTest = List.of(sportClassViewModelTest);

        SportClassViewModel matchedSportClass = sportClassServiceTest.getMatchedSportClass(
                sportClassViewModelsTest,
                1,
                14);

        assertNull(matchedSportClass);
    }

    @Test
    public void testGetMatchedSportClass_dayAndHourMatch(){
        SportClassViewModel sportClassViewModelTest =
                SportClassViewModel.builder()
                        .dayOfWeek(DayOfWeek.MONDAY)
                        .startTime(LocalTime.of(13, 0))
                        .build();

        List<SportClassViewModel> sportClassViewModelsTest = List.of(sportClassViewModelTest);

        SportClassViewModel matchedSportClass = sportClassServiceTest.getMatchedSportClass(
                sportClassViewModelsTest,
                1,
                13);

        assertNotNull(matchedSportClass);
        assertEquals(DayOfWeek.MONDAY, matchedSportClass.getDayOfWeek());
        assertEquals(LocalTime.of(13, 0), matchedSportClass.getStartTime());
    }

    //getSportClassById
    @Test
    public void testGetSportClassById_returnsSportClass(){
        SportClassEntity sportClassEntityTest = mock(SportClassEntity.class);
        SportClassBookingViewModel sportClassBookingViewModelTest = mock(SportClassBookingViewModel.class);
        Long sportClassId = 1L;

        when(sportClassRepositoryMock.findById(sportClassId))
                .thenReturn(Optional.of(sportClassEntityTest));
        when(sportClassMapperMock.sportClassEntityToBookingViewModel(sportClassEntityTest))
                .thenReturn(sportClassBookingViewModelTest);

        SportClassBookingViewModel actual = sportClassServiceTest.getSportClassById(sportClassId);

        assertEquals(sportClassBookingViewModelTest, actual);
    }

    @Test
    public void testGetSportClassById_throws(){
        Long sportClassId = 1L;

        when(sportClassRepositoryMock.findById(sportClassId))
                .thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class,
                () -> sportClassServiceTest.getSportClassById(sportClassId));

        verifyNoInteractions(sportClassMapperMock);
    }

    //hasAvailableSpots
    @Test
    public void testHasAvailableSpots_returnsTrue(){
        Long sportClassId = 1L;
        SportClassEntity sportClassEntityTest = SportClassEntity.builder()
                .currentCapacity(0)
                .maxCapacity(1)
                .build();

        when(sportClassRepositoryMock.findById(sportClassId))
                .thenReturn(Optional.of(sportClassEntityTest));

        boolean actual = sportClassServiceTest.hasAvailableSpots(sportClassId);

        assertTrue(actual);

    }

    @Test
    public void testHasAvailableSpots_returnsFalse(){
        Long sportClassId = 1L;
        SportClassEntity sportClassEntityTest = SportClassEntity.builder()
                .maxCapacity(1)
                .currentCapacity(1)
                .build();

        when(sportClassRepositoryMock.findById(sportClassId))
                .thenReturn(Optional.of(sportClassEntityTest));

        boolean actual = sportClassServiceTest.hasAvailableSpots(sportClassId);

        assertFalse(actual);
    }

    @Test
    public void testHasAvailableSpots_throws(){
        Long sportClassId = 1L;

        when(sportClassRepositoryMock.findById(sportClassId))
                .thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> sportClassServiceTest.hasAvailableSpots(sportClassId));
    }

    //updateCapacity
    @Test
    public void testUpdateCapacity_updatesCapacity(){
        Long sportClassId = 1L;
        SportClassEntity sportClassEntityTest = SportClassEntity.builder()
                .maxCapacity(1)
                .currentCapacity(0)
                .build();
        sportClassEntityTest.setId(sportClassId);

        when(sportClassRepositoryMock.findById(sportClassId))
                .thenReturn(Optional.of(sportClassEntityTest));

        sportClassServiceTest.updateCapacity(sportClassEntityTest);

        verify(sportClassRepositoryMock, times(1))
                .save(sportClassEntityTest);
        assertEquals(1, sportClassEntityTest.getCurrentCapacity());
    }

    @Test
    public void testUpdateCapacity_doesNotUpdateCapacityAndThrows() throws RuntimeException{
        Long sportClassId = 1L;
        SportEntity sportEntityTest = SportEntity.builder()
                .name("Fake")
                .description("Fake")
                .sportImageUrl("fake.com/fake.jpg")
                .build();

        InstructorEntity instructorEntityTest =
                InstructorEntity.builder()
                        .firstName("Fake")
                        .lastName("Fake")
                        .build();

        SportClassEntity sportClassEntityTest = SportClassEntity.builder()
                .sport(sportEntityTest)
                .instructor(instructorEntityTest)
                .dayOfWeek(DayOfWeek.MONDAY)
                .startTime(LocalTime.of(13, 0))
                .endTime(LocalTime.of(14, 0))
                .maxCapacity(1)
                .currentCapacity(1)
                .build();

        sportClassEntityTest.setId(sportClassId);

        when(sportClassRepositoryMock.findById(sportClassId))
                .thenReturn(Optional.of(sportClassEntityTest));

        assertThrows(UnableToProcessOperationException.class,
                () -> sportClassServiceTest.updateCapacity(sportClassEntityTest));
        verifyNoMoreInteractions(sportClassRepositoryMock);
    }

    //decreaseCapacity
    @Test
    public void testDecreaseCapacity_decreasesCapacity(){
        Long sportClassId = 1L;
        SportClassEntity sportClassEntityTest = SportClassEntity.builder()
                .maxCapacity(1)
                .currentCapacity(1)
                .build();
        sportClassEntityTest.setId(sportClassId);

        sportClassServiceTest.decreaseCapacity(sportClassEntityTest);

        verify(sportClassRepositoryMock, times(1))
                .save(sportClassEntityTest);
        assertEquals(0, sportClassEntityTest.getCurrentCapacity());
    }

    @Test
    public void testDecreaseCapacity_doesNotDecreaseCapacityAndThrows(){
        Long sportClassId = 1L;
        SportEntity sportEntityTest = SportEntity.builder()
                .name("Fake")
                .description("Fake")
                .sportImageUrl("fake.com/fake.jpg")
                .build();

        InstructorEntity instructorEntityTest =
                InstructorEntity.builder()
                        .firstName("Fake")
                        .lastName("Fake")
                        .build();

        SportClassEntity sportClassEntityTest = SportClassEntity.builder()
                .sport(sportEntityTest)
                .instructor(instructorEntityTest)
                .dayOfWeek(DayOfWeek.MONDAY)
                .startTime(LocalTime.of(13, 0))
                .endTime(LocalTime.of(14, 0))
                .maxCapacity(1)
                .currentCapacity(0)
                .build();
        sportClassEntityTest.setId(sportClassId);

        assertThrows(UnableToProcessOperationException.class,
                () -> sportClassServiceTest.decreaseCapacity(sportClassEntityTest));

        verifyNoInteractions(sportClassRepositoryMock);

        assertEquals(0, sportClassEntityTest.getCurrentCapacity());
    }

    //resetSportClassCapacityAtEndOfDay
    @Test
    public void testResetSportClassCapacityAtEndOfDay(){
        SportClassEntity sportClassEntityTest = SportClassEntity.builder()
                .currentCapacity(1)
                .build();

        when(sportClassRepositoryMock.findByDayOfWeek(any(DayOfWeek.class)))
                .thenReturn(List.of(sportClassEntityTest));

        sportClassServiceTest.resetSportClassCapacityAtEndOfDay();

        verify(sportClassRepositoryMock, times(1))
                .save(any(SportClassEntity.class));
        assertEquals(0, sportClassEntityTest.getCurrentCapacity());
    }

}
