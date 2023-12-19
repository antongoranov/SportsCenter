package com.sportscenter.service;

import com.sportscenter.exception.ObjectNotFoundException;
import com.sportscenter.model.entity.InstructorEntity;
import com.sportscenter.model.entity.SportEntity;
import com.sportscenter.model.mapper.InstructorMapper;
import com.sportscenter.model.service.AddInstructorServiceModel;
import com.sportscenter.model.view.InstructorViewModel;
import com.sportscenter.repository.InstructorRepository;
import com.sportscenter.repository.SportRepository;
import com.sportscenter.service.impl.InstructorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InstructorServiceImplTest {

    @Mock
    private InstructorRepository instructorRepositoryMock;
    @Mock
    private SportRepository sportRepositoryMock;
    @Mock
    private InstructorMapper instructorMapperMock;

    private SportEntity testSport;
    private InstructorEntity instructor1;
    private InstructorEntity instructor2;
    private InstructorViewModel instructor1View;
    private InstructorViewModel instructor2View;
    private AddInstructorServiceModel addInstructorServiceModelTest;

    @InjectMocks
    private InstructorServiceImpl instructorServiceTest;

    @BeforeEach
    public void setUp(){
        testSport = SportEntity.builder()
                .name("Testing")
                .sportImageUrl("image.com/testSport.jpg")
                .description("test desc")
                .build();

        instructor1 = InstructorEntity.builder()
                .firstName("Instructor")
                .lastName("One")
                .email("instructor1@email.com")
                .phoneNumber("+359891231231")
                .bio("test bio 1")
                .pictureUrl("image.com/instructor1.jpg")
                .sportClasses(List.of())
                .sport(testSport)
                .build();

        instructor2 = InstructorEntity.builder()
                .firstName("Instructor")
                .lastName("Two")
                .email("instructor2@email.com")
                .phoneNumber("+359892222222")
                .bio("test bio 2")
                .pictureUrl("image.com/instructor2.jpg")
                .sportClasses(List.of())
                .sport(testSport)
                .build();

        instructor1View = InstructorViewModel.builder()
                .id(1L)
                .fullName("Instructor One")
                .email("instructor1@email.com")
                .phoneNumber("+359891231231")
                .bio("test bio 1")
                .pictureUrl("image.com/instructor1.jpg")
                .sportClasses(List.of())
                .sportName(testSport.getName())
                .build();

        instructor2View = InstructorViewModel.builder()
                .fullName("Instructor Two")
                .email("instructor2@email.com")
                .phoneNumber("+359892222222")
                .bio("test bio 2")
                .pictureUrl("image.com/instructor2.jpg")
                .sportClasses(List.of())
                .sportName(testSport.getName())
                .build();

        addInstructorServiceModelTest =
                AddInstructorServiceModel.builder()
                        .firstName("Instructor")
                        .lastName("One")
                        .email("instructor1@email.com")
                        .phoneNumber("+359891231231")
                        .bio("test bio 1")
                        .pictureUrl("image.com/instructor1.jpg")
                        .sportId(1L)
                        .build();


    }


    @Test
    public void testGetAllInstructor_returnsAListOfInstructors(){

        //Arrange
        List<InstructorEntity> testInstructorsList = List.of(instructor1, instructor2);

        when(instructorRepositoryMock.findAll())
                .thenReturn(testInstructorsList);

        when(instructorMapperMock.mapInstructorEntityToViewModel(any(InstructorEntity.class)))
                .thenReturn(instructor1View, instructor2View);

        //Act
        List<InstructorViewModel> actual = instructorServiceTest.getAllInstructors();

        //Assert
        assertEquals(2, actual.size());

        assertEquals(instructor1View.getId(), actual.get(0).getId());
        assertEquals(instructor1View.getFullName(), actual.get(0).getFullName());
        assertEquals(instructor1View.getEmail(), actual.get(0).getEmail());
        assertEquals(instructor1View.getPhoneNumber(), actual.get(0).getPhoneNumber());
        assertEquals(instructor1View.getBio(), actual.get(0).getBio());
        assertEquals(instructor1View.getPictureUrl(), actual.get(0).getPictureUrl());
        assertEquals(instructor1View.getSportClasses().size(), actual.get(0).getSportClasses().size());
        assertEquals(instructor1View.getSportName(), actual.get(0).getSportName());

        assertEquals(instructor2View.getId(), actual.get(1).getId());
        assertEquals(instructor2View.getFullName(), actual.get(1).getFullName());
        assertEquals(instructor2View.getEmail(), actual.get(1).getEmail());
        assertEquals(instructor2View.getPhoneNumber(), actual.get(1).getPhoneNumber());
        assertEquals(instructor2View.getBio(), actual.get(1).getBio());
        assertEquals(instructor2View.getPictureUrl(), actual.get(1).getPictureUrl());
        assertEquals(instructor2View.getSportClasses().size(), actual.get(1).getSportClasses().size());
        assertEquals(instructor2View.getSportName(), actual.get(1).getSportName());
    }

    @Test
    public void testGetInstructorById_returnsInstructorView(){
        when(instructorRepositoryMock.findById(1L))
                .thenReturn(Optional.of(instructor1));

        when(instructorMapperMock.mapInstructorEntityToViewModel(any(InstructorEntity.class)))
                .thenReturn(instructor1View);

        InstructorViewModel actual = instructorServiceTest.getInstructorById(1L);

        assertEquals(instructor1View.getId(), actual.getId());
        assertEquals(instructor1View.getFullName(), actual.getFullName());
        assertEquals(instructor1View.getEmail(), actual.getEmail());
        assertEquals(instructor1View.getPhoneNumber(), actual.getPhoneNumber());
        assertEquals(instructor1View.getBio(), actual.getBio());
        assertEquals(instructor1View.getPictureUrl(), actual.getPictureUrl());
        assertEquals(instructor1View.getSportClasses().size(), actual.getSportClasses().size());
        assertEquals(instructor1View.getSportName(), actual.getSportName());

    }

    @Test
    public void testGetInstructorById_throwsWithNonExistentId(){
        assertThrows(ObjectNotFoundException.class,
                () -> instructorServiceTest.getInstructorById(1L));

    }


    @Test
    public void testAddInstructor_savesInstructor(){

        instructor1 = InstructorEntity.builder()
                .firstName(addInstructorServiceModelTest.getFirstName())
                .lastName(addInstructorServiceModelTest.getLastName())
                .email(addInstructorServiceModelTest.getEmail())
                .phoneNumber(addInstructorServiceModelTest.getPhoneNumber())
                .bio(addInstructorServiceModelTest.getBio())
                .pictureUrl(addInstructorServiceModelTest.getPictureUrl())
                .sportClasses(List.of())
                .build();

        when(sportRepositoryMock.findById(any()))
                .thenReturn(Optional.of(testSport));

        when(instructorMapperMock.mapInstructorServiceToEntity(any(AddInstructorServiceModel.class)))
                .thenReturn(instructor1);


        instructorServiceTest.addInstructor(addInstructorServiceModelTest);

        verify(instructorRepositoryMock, times(1))
                .save(instructor1);

    }

    @Test
    public void testAddInstructor_throwsWhenInvalidSportIdIsPassed(){
        assertThrows(ObjectNotFoundException.class,
                () -> instructorServiceTest.addInstructor(new AddInstructorServiceModel()));

    }
}
