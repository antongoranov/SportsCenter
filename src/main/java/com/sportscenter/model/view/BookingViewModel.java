package com.sportscenter.model.view;

import lombok.Data;

@Data
public class BookingViewModel {

    private Long id;

    private SportClassBookingViewModel sportClass;

    private String status;

    private boolean statusNotActive;
}
