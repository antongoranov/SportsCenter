package com.sportscenter.service.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.sportscenter.exception.ObjectNotFoundException;
import com.sportscenter.exception.QrCodeException;
import com.sportscenter.exception.UserNotFoundException;
import com.sportscenter.model.entity.BookingEntity;
import com.sportscenter.model.entity.QrCodeEntity;
import com.sportscenter.model.entity.UserEntity;
import com.sportscenter.model.mapper.QrCodeMapper;
import com.sportscenter.model.view.QrCodeViewModel;
import com.sportscenter.repository.BookingRepository;
import com.sportscenter.repository.QrCodeRepository;
import com.sportscenter.repository.UserRepository;
import com.sportscenter.service.QrCodeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class QrCodeServiceImpl implements QrCodeService {

    private final QrCodeRepository qrCodeRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final QrCodeMapper qrCodeMapper;

    @Override
    public void generateQrForBooking(BookingEntity booking) {
        byte[] qrCodeImage;

        try {
            qrCodeImage = createQrCodeImage(booking);
        } catch (IOException | WriterException e) {
            throw new QrCodeException("Error generating QR code for booking " + booking.getId());
        }

        QrCodeEntity qrCodeEntity = new QrCodeEntity(booking, qrCodeImage);

        qrCodeRepository.save(qrCodeEntity);
    }

    private byte[] createQrCodeImage(BookingEntity booking) throws WriterException, IOException {

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix =
                qrCodeWriter.encode(booking.toString(), BarcodeFormat.QR_CODE, 350, 350);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);

        return pngOutputStream.toByteArray();
    }

    @Override
    public QrCodeViewModel getQrCodeForBooking(Long bookingId) {

        BookingEntity booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ObjectNotFoundException("Booking with " + bookingId + " does not exist!"));

        QrCodeEntity qrCodeEntity = qrCodeRepository.findByBooking(booking)
                .orElseThrow(() -> new ObjectNotFoundException("QR Code for booking " + bookingId + " does not exist!"));

        QrCodeViewModel qrCodeViewModel =
                qrCodeMapper.qrEntityToViewModel(qrCodeEntity);

        String encodedQrImg = Base64.getEncoder().encodeToString(qrCodeEntity.getQrImg());

        qrCodeViewModel.setBase64EncodedQrImg(encodedQrImg);

        return qrCodeViewModel;
    }

    @Override
    @Transactional
    public void deleteQrCodeForBooking(Long bookingId) {

        BookingEntity booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ObjectNotFoundException("Booking with " + bookingId + " does not exist!"));

        qrCodeRepository.deleteByBooking(booking);
    }

    @Override
    public boolean verifyBookingOwner(Long bookingId, String username) {

        BookingEntity booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ObjectNotFoundException("Booking with " + bookingId + " does not exist!"));

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with " + username + " not found!"));

        return Objects.equals(booking.getUser().getId(), user.getId());
    }
}
