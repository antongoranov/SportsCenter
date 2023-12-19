package com.sportscenter.service;

import com.sportscenter.model.entity.SportEntity;
import com.sportscenter.model.mapper.SportMapper;
import com.sportscenter.model.view.SportViewModel;
import com.sportscenter.repository.SportRepository;
import com.sportscenter.service.impl.SportServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

}
