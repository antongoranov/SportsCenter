package com.sportscenter.model.view;

import com.sportscenter.model.entity.SportClassEntity;
import com.sportscenter.model.entity.UserEntity;
import com.sportscenter.model.enums.BookingStatusEnum;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class BookingViewModel {

    private Long id;

    private SportClassEntity sportClass;

    private String status;
}
