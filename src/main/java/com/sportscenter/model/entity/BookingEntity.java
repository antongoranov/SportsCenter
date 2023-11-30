package com.sportscenter.model.entity;

import com.sportscenter.model.enums.BookingStatusEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "bookings")
public class BookingEntity extends BaseEntity {

    @OneToOne(optional = false)
    private UserEntity client;

    @ManyToOne(optional = false)
    private SportClassEntity sportClass;

    @Column
    @Enumerated(EnumType.STRING)
    private BookingStatusEnum status;
}
