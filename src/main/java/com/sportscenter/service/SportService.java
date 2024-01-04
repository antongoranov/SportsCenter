package com.sportscenter.service;

import com.sportscenter.model.service.AddSportServiceModel;
import com.sportscenter.model.view.SportViewModel;

import java.util.List;

public interface SportService{

    List<SportViewModel> getAllSports();

    void deleteSport(Long sportId);

    boolean isSportPresent(String sportName);

    void addSport(AddSportServiceModel addSportServiceModel);
}
