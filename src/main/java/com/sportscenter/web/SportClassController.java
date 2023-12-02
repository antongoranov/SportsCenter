package com.sportscenter.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sportclass")
public class SportClassController {


    @GetMapping("/all")
    public String allSportClasses(){

        return "schedule";
    }
}
