package com.sportscenter.web;

import com.sportscenter.model.view.QrCodeViewModel;
import com.sportscenter.service.QrCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
@RequestMapping("/qrCodes")
public class QrCodeController {

    private final QrCodeService qrCodeService;

    @GetMapping("/{bookingId}")
    @PreAuthorize("@qrCodeServiceImpl.verifyBookingOwner(#bookingId, #principal.name)")
    public String getQrCodeForBooking(@PathVariable("bookingId") Long bookingId,
                                      Model model,
                                      Principal principal){
        QrCodeViewModel qrCodeForBooking =
                qrCodeService.getQrCodeForBooking(bookingId);

        model.addAttribute("qrCodeForBooking", qrCodeForBooking);

        return "qr-code-for-booking";
    }
}
