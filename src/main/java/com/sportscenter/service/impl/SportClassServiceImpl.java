package com.sportscenter.service.impl;

import com.sportscenter.exception.ObjectNotFoundException;
import com.sportscenter.exception.UnableToProcessOperationException;
import com.sportscenter.model.entity.BookingEntity;
import com.sportscenter.model.entity.InstructorEntity;
import com.sportscenter.model.entity.SportClassEntity;
import com.sportscenter.model.entity.SportEntity;
import com.sportscenter.model.enums.BookingStatusEnum;
import com.sportscenter.model.mapper.SportClassMapper;
import com.sportscenter.model.service.AddSportClassServiceModel;
import com.sportscenter.model.view.SportClassBookingViewModel;
import com.sportscenter.model.view.SportClassViewModel;
import com.sportscenter.repository.InstructorRepository;
import com.sportscenter.repository.SportClassRepository;
import com.sportscenter.service.SportClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SportClassServiceImpl implements SportClassService {

    private final SportClassRepository sportClassRepository;
    private final InstructorRepository instructorRepository;
    private final SportClassMapper sportClassMapper;

    @Override
    public List<SportClassViewModel> allClasses() {

        return sportClassRepository.findAll()
                .stream()
                .map(sportClassMapper::sportClassEntityToViewModel)
                .collect(Collectors.toList());
    }

    @Override
    public SportClassViewModel getMatchedSportClass(
            List<SportClassViewModel> sportClasses,
            int dayOfWeek,
            int startHour) {

        return sportClasses
                .stream()
                .filter(sportClass ->
                        (sportClass.getDayOfWeek().getValue() == dayOfWeek && sportClass.getStartTime().getHour() == startHour))
                .findFirst()
                .orElse(null);
    }

    @Override
    public SportClassBookingViewModel getSportClassById(Long sportClassId) {

        return sportClassRepository.findById(sportClassId)
                .map(sportClassMapper::sportClassEntityToBookingViewModel)
                //log
                .orElseThrow(() -> new ObjectNotFoundException("Sport class with " + sportClassId + " not found!"));
    }

    @Override
    public boolean hasAvailableSpots(Long sportClassId) {

        SportClassEntity sportClass = sportClassRepository.findById(sportClassId)
                .orElseThrow();

        return sportClass.getCurrentCapacity() + 1 <= sportClass.getMaxCapacity();
    }

    @Override
    public void updateCapacity(SportClassEntity sportClass) {
        if (hasAvailableSpots(sportClass.getId())) {
            sportClass.setCurrentCapacity(sportClass.getCurrentCapacity() + 1);

            sportClassRepository.save(sportClass);
        } else {
            throw new UnableToProcessOperationException("No spots available! Cannot increase capacity of " + sportClass.getSportClassInfo());
        }
    }

    @Override
    public void decreaseCapacity(SportClassEntity sportClass) {
        if(sportClass.getCurrentCapacity() - 1 >= 0) {
            sportClass.setCurrentCapacity(sportClass.getCurrentCapacity() - 1);

            sportClassRepository.save(sportClass);
        } else {
            throw new UnableToProcessOperationException("Cannot decrease capacity below 0 of " + sportClass.getSportClassInfo());
        }
    }

    @Override
    public boolean isTimeSlotTaken(DayOfWeek newDayOfWeek,
                                   LocalTime newStartTime,
                                   LocalTime newEndTime) {

        return sportClassRepository.findAll()
                .stream()
                .anyMatch(sportClass -> {
                    return sportClass.getDayOfWeek() == newDayOfWeek &&
                            sportClass.getStartTime().equals(newStartTime) &&
                            sportClass.getEndTime().equals(newEndTime);
                });
    }

    @Override
    public void addSportClass(AddSportClassServiceModel addSportClass) {

        InstructorEntity instructor = instructorRepository.findById(addSportClass.getInstructorId())
                .orElseThrow(() ->
                        new ObjectNotFoundException(
                                "Instructor with id: " + addSportClass.getInstructorId() + " does not exist!"));

        SportEntity instructorSport = instructor.getSport();

        SportClassEntity newSportClass = SportClassEntity.builder()
                .sport(instructorSport)
                .instructor(instructor)
                .maxCapacity(addSportClass.getMaxCapacity())
                .currentCapacity(0)
                .dayOfWeek(addSportClass.getDayOfWeek())
                .startTime(addSportClass.getStartTime())
                .endTime(addSportClass.getEndTime())
                .build();

        sportClassRepository.save(newSportClass);

    }

    //***Scheduled tasks***
    //Everyday at midnight set the capacity of passed classes to 0 for the next week bookings
    @Override
    @Scheduled(cron = "0 59 23 * * ?")
    public void resetSportClassCapacityAtEndOfDay(){
        DayOfWeek today = LocalDateTime.now().getDayOfWeek();

        List<SportClassEntity> sportClassesToReset =
                sportClassRepository.findByDayOfWeek(today);

        sportClassesToReset
                .forEach(sportClass -> {
                    sportClass.setCurrentCapacity(0);
                    sportClassRepository.save(sportClass);
                });
    }
}
