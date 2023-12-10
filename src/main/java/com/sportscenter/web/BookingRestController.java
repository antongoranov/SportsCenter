package com.sportscenter.web;

import com.sportscenter.model.view.BookingViewModel;
import com.sportscenter.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class BookingRestController {

    private final BookingService bookingService;

    @GetMapping("/myBookings")
    public ResponseEntity<List<BookingViewModel>> userBookings(@AuthenticationPrincipal UserDetails userDetails){

        List<BookingViewModel> bookingsByUser = bookingService.findBookingsByUser(userDetails);

        return ResponseEntity.ok(bookingsByUser);
    }
}
