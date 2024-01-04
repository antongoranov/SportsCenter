package com.sportscenter.web;

import com.sportscenter.model.view.SportViewModel;
import com.sportscenter.service.SportService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/sports")
public class SportController {

    private final SportService sportService;

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

    //TODO: Add Sport
}
