package com.sportscenter.web;

import com.sportscenter.model.view.BookingViewModel;
import com.sportscenter.model.view.SportClassBookingViewModel;
import com.sportscenter.service.BookingService;
import com.sportscenter.service.SportClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RequiredArgsConstructor
@Controller
//@RequestMapping("/bookSportClass")
public class BookingController {

    private final BookingService bookingService;
    private final SportClassService sportClassService;

    @GetMapping("/bookSportClass/{sportClassId}")
    public String getBookSportClass(@PathVariable Long sportClassId, Model model){

        SportClassBookingViewModel sportClass = sportClassService.getSportClassById(sportClassId);

        model.addAttribute("sportClass", sportClass);

        boolean hasAvailableSportClassSpots = sportClassService.hasAvailableSpots(sportClassId);
        if(!hasAvailableSportClassSpots) {
            model.addAttribute("noAvailableSpots", true);
        }

            return "sport-class-book";
    }

    //@PreAuthorize(hasAvailableSpots)
    @PostMapping("/bookSportClass/{sportClassId}")
    public String bookSportClass(@AuthenticationPrincipal UserDetails userDetails,
                                 @PathVariable Long sportClassId,
                                 RedirectAttributes redirectAttributes){

        boolean hasActiveBookings = bookingService.hasActiveBookings(userDetails);
        if(hasActiveBookings) {
            redirectAttributes.addFlashAttribute("hasActiveBookings", true);

            return "redirect:/bookSportClass/" + sportClassId;
        }

        bookingService.bookASportClass(userDetails, sportClassId);

        return "redirect:/myBookings";
    }

    @GetMapping("/myBookings")
    public String userBookings(@AuthenticationPrincipal UserDetails userDetails, Model model){

        List<BookingViewModel> bookingsByUser = bookingService.findBookingsByUser(userDetails);

        model.addAttribute("bookingsByUser", bookingsByUser);

        return "user-bookings";
    }

//    @PreAuthorize("@bookingServiceImpl.isUserIssuerOfBooking(#userDetails, #bookingId)")
    @PutMapping("/myBookings/cancelBooking/{bookingId}")
    public String cancelBooking(//@AuthenticationPrincipal UserDetails userDetails,
                                @PathVariable Long bookingId){

        bookingService.cancelBooking(bookingId);

        return "redirect:/myBookings";
    }
}
