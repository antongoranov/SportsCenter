package com.sportscenter.web;

import com.sportscenter.model.view.InstructorViewModel;
import com.sportscenter.service.InstructorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/instructors")
public class InstructorController {

    private final InstructorService instructorService;

    @GetMapping("/all")
    public String allInstructors(Model model){

        List<InstructorViewModel> instructors = instructorService.getAllInstructors();

        model.addAttribute("instructors", instructors);

        return "instructors-all";
    }


}
