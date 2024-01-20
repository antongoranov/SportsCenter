package com.sportscenter.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "qr_codes")
public class QrCodeEntity extends BaseEntity {

    @OneToOne(optional = false)
    private BookingEntity booking;

    @Column(name = "qr_img", nullable = false, length = 1000)
    private byte[] qrImg;

}
