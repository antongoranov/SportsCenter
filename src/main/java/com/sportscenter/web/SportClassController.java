package com.sportscenter.web;

import com.sportscenter.model.view.SportClassViewModel;
import com.sportscenter.service.SportClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/sportclasses")
public class SportClassController {

    private final SportClassService sportClassService;

    @GetMapping("/all")
    public String allSportClasses(Model model){
        //obtain all sport classes
        List<SportClassViewModel> sportClasses = sportClassService.allClasses();

        //return them to the view
        model.addAttribute("sportClasses", sportClasses);

        return "schedule";
    }
}
