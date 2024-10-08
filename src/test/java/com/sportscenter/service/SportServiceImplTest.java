package com.sportscenter.service;

import com.sportscenter.exception.ObjectNotFoundException;
import com.sportscenter.model.entity.SportEntity;
import com.sportscenter.model.mapper.SportMapper;
import com.sportscenter.model.service.AddSportServiceModel;
import com.sportscenter.model.view.SportViewModel;
import com.sportscenter.repository.SportRepository;
import com.sportscenter.service.impl.SportServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SportServiceImplTest {

    @Mock
    private SportRepository sportRepositoryMock;
    @Mock
    private SportMapper sportMapperMock;

    @InjectMocks
    private SportServiceImpl sportServiceMock;

    private SportEntity testSportEntity;

    @BeforeEach
    public void setUp(){
        testSportEntity = SportEntity.builder()
                .name("Test")
                .description("Test Desc")
                .sportImageUrl("testUrl.jpg")
                .instructors(new HashSet<>())
                .build();
    }

    @Test
    public void testGetAllSport_returnsAList(){
        List<SportEntity> sportEntityListTest = Collections.singletonList(new SportEntity());
        when(sportRepositoryMock.findAll())
                .thenReturn(sportEntityListTest);

        List<SportViewModel> mockSportViewModels = Collections.singletonList(new SportViewModel());
        when(sportMapperMock.sportEntityToViewModel(any(SportEntity.class)))
                .thenReturn(mockSportViewModels.get(0));


        List<SportViewModel> actual = sportServiceMock.getAllSports();


        assertEquals(mockSportViewModels.size(), actual.size());
        verify(sportMapperMock, times(sportEntityListTest.size()))
                .sportEntityToViewModel(any(SportEntity.class));
    }

    //deleteSport
    @Test
    public void testDeleteSport_deletesSportWhenFound(){

        when(sportRepositoryMock.findById(1L))
                .thenReturn(Optional.of(testSportEntity));

        sportServiceMock.deleteSport(1L);

        verify(sportRepositoryMock, times(1))
                .delete(testSportEntity);

    }

    @Test
    public void testDeleteSport_throwsWhenSportNotFound(){
        when(sportRepositoryMock.findById(2L))
                .thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class,
                () -> sportServiceMock.deleteSport(2L));
    }

    //addSport
    @Test
    public void testAddSport_addsSportSuccessfully(){
        AddSportServiceModel addSportServiceModelTest =
                new AddSportServiceModel("Test", "Test Desc", "testUrl.jpg");

        when(sportMapperMock.addSportServiceModelToSportEntity(addSportServiceModelTest))
                .thenReturn(testSportEntity);

        sportServiceMock.addSport(addSportServiceModelTest);

        verify(sportMapperMock, times(1))
                .addSportServiceModelToSportEntity(addSportServiceModelTest);
        verify(sportRepositoryMock, times(1))
                .save(testSportEntity);
    }

    //isSportPresent
    @Test
    public void testIsSportPresent_returnsTrueWhenPresent(){
        when(sportRepositoryMock.findByName("Test"))
                .thenReturn(Optional.of(testSportEntity));

        assertTrue(sportServiceMock.isSportPresent("Test"));
    }

    @Test
    public void testIsSportPresent_returnsFalseWhenMissing(){
        when(sportRepositoryMock.findByName("Missing"))
                .thenReturn(Optional.empty());

        assertFalse(sportServiceMock.isSportPresent("Missing"));
    }

}
