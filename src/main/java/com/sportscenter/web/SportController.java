package com.sportscenter.web;

import com.sportscenter.model.binding.AddSportBindingModel;
import com.sportscenter.model.mapper.SportMapper;
import com.sportscenter.model.service.AddSportServiceModel;
import com.sportscenter.model.view.SportViewModel;
import com.sportscenter.service.SportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/sports")
public class SportController {

    private final SportService sportService;
    private final SportMapper sportMapper;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/all")
    public String allSports(Model model){
        List<SportViewModel> allSports = sportService.getAllSports();
        model.addAttribute("allSports", allSports);
        return "sports-all";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{sportId}/delete")
    public String deleteSport(@PathVariable("sportId") Long sportId){
        sportService.deleteSport(sportId);
        return "redirect:/sports/all";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/add")
    public String getAddSport(Model model){

        if(!model.containsAttribute("addSportBindingModel")){
            model.addAttribute(new AddSportBindingModel());
        }

        return "sport-add";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    public String addSport(@Valid AddSportBindingModel addSportBindingModel,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes){

        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("addSportBindingModel", addSportBindingModel);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.addSportBindingModel", bindingResult);

            return "redirect:/sports/add";
        }

        if(sportService.isSportPresent(addSportBindingModel.getName())){
            redirectAttributes.addFlashAttribute("addSportBindingModel", addSportBindingModel);
            redirectAttributes.addFlashAttribute("sportAvailable", true);

            return "redirect:/sports/add";
        }

        AddSportServiceModel addSportServiceModel =
                sportMapper.addSportBindingModelToServiceModel(addSportBindingModel);

        sportService.addSport(addSportServiceModel);

        return "redirect:/sports/all";
    }

}
