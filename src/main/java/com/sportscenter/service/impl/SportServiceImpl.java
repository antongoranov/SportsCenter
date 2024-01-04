package com.sportscenter.service.impl;

import com.sportscenter.exception.ObjectNotFoundException;
import com.sportscenter.model.entity.SportEntity;
import com.sportscenter.model.mapper.SportMapper;
import com.sportscenter.model.view.SportViewModel;
import com.sportscenter.repository.SportRepository;
import com.sportscenter.service.SportService;
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
    public void deleteSport(Long sportId) {
        SportEntity sport = sportRepository.findById(sportId)
                .orElseThrow(() -> new ObjectNotFoundException("Sport with " + sportId + " not found!"));
        sportRepository.delete(sport);
    }
}
