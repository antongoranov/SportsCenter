package com.sportscenter.service.impl;

import com.sportscenter.exception.ObjectNotFoundException;
import com.sportscenter.model.entity.SportEntity;
import com.sportscenter.model.mapper.SportMapper;
import com.sportscenter.model.service.AddSportServiceModel;
import com.sportscenter.model.view.SportViewModel;
import com.sportscenter.repository.SportRepository;
import com.sportscenter.service.SportService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SportServiceImpl implements SportService {

    private final SportRepository sportRepository;
    private final SportMapper sportMapper;

    @Override
    public List<SportViewModel> getAllSports() {
        return sportRepository
                .findAll()
                .stream()
                .map(sportMapper::sportEntityToViewModel)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional //check if it is needed
    public void deleteSport(Long sportId) {
        SportEntity sport = sportRepository.findById(sportId)
                .orElseThrow(() -> new ObjectNotFoundException("Sport with " + sportId + " not found!"));
        sportRepository.delete(sport);
    }

    @Override
    public void addSport(AddSportServiceModel addSportServiceModel) {
        SportEntity newSport =
                sportMapper.addSportServiceModelToSportEntity(addSportServiceModel);

        sportRepository.save(newSport);
    }

    @Override
    public boolean isSportPresent(String sportName) {
        return sportRepository
                .findByName(sportName)
                .isPresent();
    }
}
