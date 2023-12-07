package com.sportscenter.service.impl;

import com.sportscenter.exception.SportClassNotFoundException;
import com.sportscenter.model.entity.SportClassEntity;
import com.sportscenter.model.mapper.SportClassMapper;
import com.sportscenter.model.view.SportClassBookingViewModel;
import com.sportscenter.model.view.SportClassViewModel;
import com.sportscenter.repository.SportClassRepository;
import com.sportscenter.service.SportClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SportClassServiceImpl implements SportClassService {

    private final SportClassRepository sportClassRepository;
    private final SportClassMapper sportClassMapper;

    @Override
    public List<SportClassViewModel> allClasses() {

        //fetch all classes and map them to view dto
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
                //use LOGGER
                .orElseThrow(() -> new SportClassNotFoundException("Sport class with " + sportClassId + " not found!"));
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

            //update
            sportClassRepository.save(sportClass);
        }
    }
}
