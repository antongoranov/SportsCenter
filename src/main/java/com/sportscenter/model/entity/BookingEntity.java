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

    @ManyToOne(optional = false)
    private UserEntity user;

    @ManyToOne(optional = false)
    private SportClassEntity sportClass;

    @Column
    @Enumerated(EnumType.STRING)
    private BookingStatusEnum status;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    private QrCodeEntity qrCode;

    @Override
    public String toString() {
        return String.format("%s booked %s with status %s",
                user.getUsername(),
                sportClass.getSportClassInfo(),
                status.toString());
    }
}
