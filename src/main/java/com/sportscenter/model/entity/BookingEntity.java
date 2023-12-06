package com.sportscenter.model.entity;

import com.sportscenter.model.enums.BookingStatusEnum;
import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "bookings")
public class BookingEntity extends BaseEntity {

    @OneToOne(optional = false)
    private UserEntity user;

    @ManyToOne(optional = false)
    private SportClassEntity sportClass;

    @Column
    @Enumerated(EnumType.STRING)
    private BookingStatusEnum status;
}
