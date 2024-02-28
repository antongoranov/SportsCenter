package com.sportscenter.service.impl;

import com.sportscenter.exception.ObjectNotFoundException;
import com.sportscenter.model.entity.InstructorEntity;
import com.sportscenter.model.entity.SportEntity;
import com.sportscenter.model.mapper.InstructorMapper;
import com.sportscenter.model.service.AddInstructorServiceModel;
import com.sportscenter.model.view.InstructorViewModel;
import com.sportscenter.repository.InstructorRepository;
import com.sportscenter.repository.SportRepository;
import com.sportscenter.service.InstructorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class InstructorServiceImpl implements InstructorService {

    private final InstructorRepository instructorRepository;
    private final SportRepository sportRepository;

    private final InstructorMapper instructorMapper;


    @Override
    public List<InstructorViewModel> getAllInstructors() {
        return instructorRepository.findAll()
                .stream()
                .map(instructorMapper::mapInstructorEntityToViewModel)
                .collect(Collectors.toList());
    }
    @Override
    public Page<InstructorViewModel> getAllInstructorsPaged(Pageable pageable) {
        return instructorRepository.findAll(pageable)
                .map(instructorMapper::mapInstructorEntityToViewModel);
    }

    @Override
    public InstructorViewModel getInstructorById(Long id) {

        return instructorRepository.findById(id)
                .map(instructorMapper::mapInstructorEntityToViewModel)
                .orElseThrow(() ->
                        new ObjectNotFoundException("Instructor with " + id + " does not exist!"));
    }

    @Override
    public void addInstructor(AddInstructorServiceModel addInstructorServiceModel) {

        SportEntity sport = sportRepository.findById(addInstructorServiceModel.getSportId())
                .orElseThrow(() ->
                        new ObjectNotFoundException("Sport with " + addInstructorServiceModel.getSportId() + " not found!"));

        InstructorEntity newInstructor =
                instructorMapper.mapInstructorServiceToEntity(addInstructorServiceModel);

        newInstructor.setSport(sport);

        instructorRepository.save(newInstructor);
    }
}
