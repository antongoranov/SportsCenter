package com.sportscenter.web;

import com.sportscenter.model.view.SportClassBookingViewModel;
import com.sportscenter.service.BookingService;
import com.sportscenter.service.SportClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/bookSportClass")
public class BookingController {

    private final BookingService bookingService;

    private final SportClassService sportClassService;

    @GetMapping("/{sportClassId}")
    public String getBookSportClass(@PathVariable Long sportClassId, Model model){

        SportClassBookingViewModel sportClass = sportClassService.getSportClassById(sportClassId);

        model.addAttribute("sportClass", sportClass);

        return "sport-class-book";
    }

    @PostMapping("/{sportClassId}")
    public String bookSportClass(@AuthenticationPrincipal UserDetails userDetails,
                                 @PathVariable Long sportClassId){

        bookingService.bookASportClass(userDetails, sportClassId);

        //implement a success page or redirect to MyBookings page
        return "redirect:/";
    }
}
