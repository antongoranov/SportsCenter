package com.sportscenter.service.impl;

import com.sportscenter.model.mapper.InstructorMapper;
import com.sportscenter.model.view.InstructorViewModel;
import com.sportscenter.repository.InstructorRepository;
import com.sportscenter.service.InstructorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class InstructorServiceImpl implements InstructorService {

    private final InstructorRepository instructorRepository;
    private final InstructorMapper instructorMapper;

    @Override
    public List<InstructorViewModel> getAllInstructors() {
        return instructorRepository.findAll()
                .stream()
                .map(instructorMapper::mapInstructorEntityToViewModel)
                .collect(Collectors.toList());
    }
}
