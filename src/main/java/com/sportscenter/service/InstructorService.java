package com.sportscenter.service;

import com.sportscenter.model.service.AddInstructorServiceModel;
import com.sportscenter.model.view.InstructorViewModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InstructorService {

    List<InstructorViewModel> getAllInstructors();

    Page<InstructorViewModel> getAllInstructorsPaged(Pageable pageable);

    InstructorViewModel getInstructorById(Long id);
    void addInstructor(AddInstructorServiceModel serviceModel);
}
