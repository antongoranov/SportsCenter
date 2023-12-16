package com.sportscenter.web;

import com.sportscenter.model.binding.AddSportClassBindingModel;
import com.sportscenter.model.mapper.SportClassMapper;
import com.sportscenter.model.service.AddSportClassServiceModel;
import com.sportscenter.model.view.SportClassViewModel;
import com.sportscenter.service.InstructorService;
import com.sportscenter.service.SportClassService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/sportClasses")
public class SportClassController {

    private final SportClassService sportClassService;
    private final InstructorService instructorService;
    private final SportClassMapper sportClassMapper;

    @GetMapping("/all")
    public String allSportClasses(Model model){

        List<SportClassViewModel> sportClasses = sportClassService.allClasses();
        model.addAttribute("sportClasses", sportClasses);

        return "schedule";
    }

    //***ADMIN***
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/add")
    public String getAddSportClass(Model model) {

        if(!model.containsAttribute("sportClass")){
            model.addAttribute("sportClass", new AddSportClassBindingModel());
        }

        List<SportClassViewModel> sportClasses = sportClassService.allClasses();
        model.addAttribute("sportClasses", sportClasses);
        model.addAttribute("allInstructors", instructorService.getAllInstructors());

        return "sport-class-add";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public String addSportClass(@ModelAttribute("sportClass") @Valid AddSportClassBindingModel addSportClassBindingModel,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {

        //Validate form inputs
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("sportClass", addSportClassBindingModel);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.sportClass", bindingResult);

            return "redirect:/sportClasses/add";
        }

        DayOfWeek newDayOfWeek = addSportClassBindingModel.getDayOfWeek();
        LocalTime newStartTime = addSportClassBindingModel.getStartTime();
        LocalTime newEndTime = addSportClassBindingModel.getEndTime();

        boolean isTimeSlotTaken =
                sportClassService.isTimeSlotTaken(newDayOfWeek, newStartTime, newEndTime);

        //validate availability
        if (isTimeSlotTaken) {
            redirectAttributes.addFlashAttribute("sportClass", addSportClassBindingModel);
            redirectAttributes.addFlashAttribute("timeSlotTaken", true);
            return "redirect:/sportClasses/add";
        }

        AddSportClassServiceModel addSportClassServiceModel =
                sportClassMapper.mapAddSportClassBindingModelToAddSportClassService(addSportClassBindingModel);

        sportClassService.addSportClass(addSportClassServiceModel);

        return "redirect:/sportClasses/all";
    }
}
