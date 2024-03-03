package com.sportscenter.web;

import com.sportscenter.model.binding.UserSearchBindingModel;
import com.sportscenter.model.service.UserSearchServiceModel;
import com.sportscenter.model.view.BookingViewModel;
import com.sportscenter.model.view.SportClassBookingViewModel;
import com.sportscenter.service.BookingService;
import com.sportscenter.service.SportClassService;
import com.sportscenter.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class BookingController {

    private final BookingService bookingService;
    private final SportClassService sportClassService;
    private final UserService userService;

    @GetMapping("/bookSportClass/{sportClassId}")
    public String getBookSportClass(@PathVariable Long sportClassId, Model model) {

        SportClassBookingViewModel sportClass = sportClassService.getSportClassById(sportClassId);

        model.addAttribute("sportClass", sportClass);

        boolean hasAvailableSportClassSpots = sportClassService.hasAvailableSpots(sportClassId);
        if (!hasAvailableSportClassSpots) {
            model.addAttribute("noAvailableSpots", true);
        }

        return "sport-class-book";
    }

    @PostMapping("/bookSportClass/{sportClassId}")
    public String bookSportClass(@AuthenticationPrincipal UserDetails userDetails,
                                 @PathVariable Long sportClassId,
                                 RedirectAttributes redirectAttributes) {

        boolean hasActiveBookings = bookingService.hasActiveBookings(userDetails);
        if (hasActiveBookings) {
            redirectAttributes.addFlashAttribute("hasActiveBookings", true);

            return "redirect:/bookSportClass/" + sportClassId;
        }

        bookingService.bookASportClass(userDetails, sportClassId);

        return "redirect:/myBookings";
    }

    @GetMapping("/myBookings")
    public String userBookings(@AuthenticationPrincipal UserDetails userDetails, Model model) {

        List<BookingViewModel> bookingsByUser = bookingService.findBookingsByUser(userDetails);

        model.addAttribute("bookingsByUser", bookingsByUser);

        return "user-bookings";
    }

    @PatchMapping("/myBookings/cancelBooking/{bookingId}")
    public String cancelBooking(@PathVariable Long bookingId) {
        bookingService.cancelBooking(bookingId);
        return "redirect:/myBookings";
    }



    //*****ADMIN*****
    //All Bookings
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/bookings/all")
    public String allBookings(UserSearchBindingModel userSearchBindingModel,
                              Model model) {

        if (!model.containsAttribute("userSearch")) {
            model.addAttribute("userSearch", userSearchBindingModel);
        }

        if (!userSearchBindingModel.isEmpty()) {

            if(!userService.userExists(userSearchBindingModel.getUsername())) {
                model.addAttribute("userNotExist",  true);

                return "search-user-bookings";
            }

            UserSearchServiceModel userSearchServiceModel = UserSearchServiceModel.builder()
                    .username(userSearchBindingModel.getUsername())
                    .build();

            List<BookingViewModel> allBookings =
                    bookingService.findBookingsByUsername(userSearchServiceModel);

            model.addAttribute("allBookingsByUser", allBookings);
        }

        return "search-user-bookings";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/bookings/all/cancelBooking/{bookingId}")
    public String cancelBookingAdmin(@PathVariable Long bookingId) {

        bookingService.cancelBooking(bookingId);
        return "redirect:/bookings/all";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/bookings/all/acceptBooking/{bookingId}")
    public String acceptBooking(@PathVariable Long bookingId){

        bookingService.acceptBooking(bookingId);
        return "redirect:/bookings/all";
    }
}
