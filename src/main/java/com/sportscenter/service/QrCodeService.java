package com.sportscenter.service;

import com.sportscenter.model.entity.BookingEntity;
import com.sportscenter.model.view.QrCodeViewModel;

public interface QrCodeService {

    void generateQrForBooking(BookingEntity booking);

    QrCodeViewModel getQrCodeForBooking(Long bookingId);

    void deleteQrCodeForBooking(Long bookingId);

    boolean verifyBookingOwner(Long bookingId, String username);
}
