package com.sportscenter.model.view;

import com.sportscenter.model.entity.SportClassEntity;
import lombok.Data;

@Data
public class BookingViewModel {

    //booking id
    private Long id;

    //use dto instead of entity
    //private SportClassEntity sportClass;
    private SportClassBookingViewModel sportClass;

    private String status;

    private boolean statusNotActive;
}
