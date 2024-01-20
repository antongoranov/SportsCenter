package com.sportscenter.model.view;

import lombok.Data;

@Data
public class QrCodeViewModel {

    private BookingViewModel booking;
    private String base64EncodedQrImg;
}
