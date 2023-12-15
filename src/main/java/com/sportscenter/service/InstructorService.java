package com.sportscenter.service;

import com.sportscenter.model.service.AddInstructorServiceModel;
import com.sportscenter.model.view.InstructorViewModel;

import java.util.List;

public interface InstructorService {

    List<InstructorViewModel> getAllInstructors();
    InstructorViewModel getInstructorById(Long id);

    void addInstructor(AddInstructorServiceModel serviceModel);
}
