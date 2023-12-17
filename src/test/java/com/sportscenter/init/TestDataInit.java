package com.sportscenter.init;

import com.sportscenter.model.entity.*;
import com.sportscenter.model.enums.UserRoleEnum;
import com.sportscenter.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class TestDataInit {

    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final SportRepository sportRepository;
    private final SportClassRepository sportClassRepository;
    private final InstructorRepository instructorRepository;


    private void initRoles() {
        if (userRoleRepository.count() == 0) {
            UserRoleEntity testUserRoleAdmin = new UserRoleEntity(UserRoleEnum.ADMIN);
            UserRoleEntity testUserRoleUser = new UserRoleEntity(UserRoleEnum.USER);

            userRoleRepository.saveAll(List.of(testUserRoleAdmin, testUserRoleUser));
        }
    }

    public UserEntity initAdmin(){

        initRoles();

        UserEntity testAdmin = UserEntity.builder()
                .firstName("Admin")
                .lastName("Adminov")
                .username("admin")
                .email("admin@email.com")
                .password("12345")
                .profilePictureUrl("url/testAdmin")
                .roles(new HashSet<>(userRoleRepository.findAll()))
                .build();

        return userRepository.save(testAdmin);
    }

    public UserEntity initUser(){
        initRoles();

        UserEntity testUser = UserEntity.builder()
                .firstName("User")
                .lastName("Userov")
                .username("user")
                .email("user@email.com")
                .password("12345")
                .profilePictureUrl("url/testUser")
                .roles(Set.of(userRoleRepository.findByRole(UserRoleEnum.USER)))
                .build();

        return userRepository.save(testUser);
    }

    public SportClassEntity initSportClass(){

        InstructorEntity testInstructor = initInstructor();

        SportClassEntity sportClass = SportClassEntity.builder()
                .instructor(testInstructor)
                .sport(testInstructor.getSport())
                .currentCapacity(0)
                .maxCapacity(20)
                .dayOfWeek(DayOfWeek.MONDAY)
                .startTime(LocalTime.of(10,0))
                .endTime(LocalTime.of(11,0))
                .build();

        return sportClassRepository.save(sportClass);
    }

    public InstructorEntity initInstructor() {

        SportEntity testSport = initSport();

        InstructorEntity instructor = InstructorEntity.builder()
                .firstName("Ivan")
                .lastName("Stoqnov")
                .email("test email")
                .bio("test bio")
                .phoneNumber("+359234567890")
                .pictureUrl("instructor.com/image.jpg")
                .sport(testSport)
                .build();

        return instructorRepository.save(instructor);
    }

    private SportEntity initSport() {
        SportEntity sport = SportEntity.builder()
                .name("test sport")
                .description("test sport dec")
                .sportImageUrl("sport.com/image.jpg")
                .build();

        return sportRepository.save(sport);
    }


    public void clearDB(){

        userRepository.deleteAll();
        userRoleRepository.deleteAll();

        sportRepository.deleteAll();

        sportClassRepository.deleteAll();
        instructorRepository.deleteAll();
    }


}
