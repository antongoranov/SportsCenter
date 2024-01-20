package com.sportscenter.repository;

import com.sportscenter.model.entity.BookingEntity;
import com.sportscenter.model.entity.QrCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QrCodeRepository extends JpaRepository<QrCodeEntity, Long> {
    Optional<QrCodeEntity> findByBooking(BookingEntity booking);

    void deleteByBooking(BookingEntity booking);
}
