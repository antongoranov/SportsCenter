package com.sportscenter.web;

import com.sportscenter.model.binding.AddInstructorBindingModel;
import com.sportscenter.model.mapper.InstructorMapper;
import com.sportscenter.model.service.AddInstructorServiceModel;
import com.sportscenter.model.view.InstructorViewModel;
import com.sportscenter.model.view.SportViewModel;
import com.sportscenter.service.InstructorService;
import com.sportscenter.service.SportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/instructors")
public class InstructorController {

    private final InstructorService instructorService;
    private final SportService sportService;
    private final InstructorMapper instructorMapper;

    @GetMapping("/all")
    public String allInstructors(Model model,
                                 @PageableDefault(size = 4) Pageable pageable) {

        Page<InstructorViewModel> instructors =
                instructorService.getAllInstructorsPaged(pageable);

        model.addAttribute("instructors", instructors);

        return "instructors-all";
    }

    @GetMapping("/{id}")
    public String instructorInfo(@PathVariable Long id,
                                 Model model) {

        InstructorViewModel instructor = instructorService.getInstructorById(id);

        model.addAttribute("instructor", instructor);

        return "instructor-info";
    }


    //*****ADMIN*****
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/add")
    public String getAddInstructor(Model model) {

        if (!model.containsAttribute("addInstructorBindingModel")) {
            model.addAttribute("addInstructorBindingModel", new AddInstructorBindingModel());
        }

        List<SportViewModel> allSports = sportService.getAllSports();

        model.addAttribute("allSports", allSports);

        return "instructor-add";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    public String addInstructor(@Valid AddInstructorBindingModel addInstructorBindingModel,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addInstructorBindingModel", addInstructorBindingModel);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.addInstructorBindingModel", bindingResult);

            return "redirect:/instructors/add";
        }

        AddInstructorServiceModel serviceModel =
                instructorMapper.mapInstructorBindingToService(addInstructorBindingModel);

        instructorService.addInstructor(serviceModel);


        return "redirect:/instructors/all";
    }
}
