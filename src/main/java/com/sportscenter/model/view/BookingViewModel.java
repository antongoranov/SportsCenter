package com.sportscenter.model.view;

import com.sportscenter.model.entity.SportClassEntity;
import lombok.Data;

@Data
public class BookingViewModel {

    private Long id;

    private SportClassEntity sportClass;

    private String status;

    private boolean statusNotActive;
}
